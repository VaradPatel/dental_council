package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchCountOnCardInnerResponseTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserGroup;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IStatusWiseCount;
import in.gov.abdm.nmr.mapper.IStatusWiseCountMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@Service
public class FetchCountOnCardServiceImpl implements IFetchCountOnCardService {

    /**
     * Injecting IFetchCountOnCardRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchCountOnCardRepository iFetchCountOnCardRepository;

    @Autowired
    private IHpProfileRepository iHpProfileRepository;

    /**
     * Mapper Interface to transform the StatusWiseCount Bean
     * to the StatusWiseCountTO Bean transferring its contents
     */
    @Autowired
    private IStatusWiseCountMapper iStatusWiseCountMapper;

    @Autowired
    private IAccessControlService accessControlService;

    @Autowired
    private ICollegeDeanRepository iCollegeDeanRepository;

    @Autowired
    private ICollegeRegistrarRepository iCollegeRegistrarRepository;

    @Autowired
    private ICollegeRepository iCollegeRepository;

    @Autowired
    private ISmcProfileRepository iSmcProfileRepository;

    @Autowired
    private INbeProfileRepository iNbeProfileRepository;

    private BigInteger counter=BigInteger.ZERO;

    private BigInteger tempCount=BigInteger.ZERO;

    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard() throws InvalidRequestException, AccessDeniedException {

        User loggedInUser = accessControlService.getLoggedInUser();
        String groupName = loggedInUser.getGroup().getName();
        if (loggedInUser == null) {
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
        }

        FetchCountOnCardResponseTO responseTO = new FetchCountOnCardResponseTO();

        /**
         * Data retrieval - HP Registration
         */
        List<String> filteredStatusListForRegistration = new ArrayList<>();
        responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_REGISTRATION.getId().toString()+COMMA_SEPARATOR+ApplicationType.FOREIGN_HP_REGISTRATION.getId().toString())
                .build());
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE_ADMIN.getDescription().equals(groupName) || Group.COLLEGE_DEAN.getDescription().equals(groupName) || Group.COLLEGE_REGISTRAR.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter=BigInteger.ZERO;
            List<StatusWiseCountTO> hpRegistrationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_REGISTRATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        filteredStatusListForRegistration.add(statusWiseCountTO.getName());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempHpRegistrationRequests = hpRegistrationRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForRegistration.contains(workflowStatus.getDescription())) {
                    tempHpRegistrationRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .build());
                }
            });

            tempHpRegistrationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_HP_REGISTRATION_REQUESTS)
                    .count(counter)
                    .build());

            responseTO.getHpRegistrationRequest().setStatusWiseCount(tempHpRegistrationRequests);
        }

        /**
         * Data retrieval - HP Modification
         */
        List<String> filteredStatusListForModification = new ArrayList<>();
        responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_MODIFICATION.getId().toString()+COMMA_SEPARATOR+ApplicationType.FOREIGN_HP_MODIFICATION.getId().toString())
                .build());
        counter = BigInteger.ZERO;
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE_ADMIN.getDescription().equals(groupName) || Group.COLLEGE_DEAN.getDescription().equals(groupName) || Group.COLLEGE_REGISTRAR.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName) || Group.NBE.getDescription().equals(groupName)) {
            List<StatusWiseCountTO> hpModificationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_MODIFICATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        filteredStatusListForModification.add(statusWiseCountTO.getName());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempHpModificationRequests = hpModificationRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForModification.contains(workflowStatus.getDescription())) {
                    tempHpModificationRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .build());
                }
            });

            tempHpModificationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_HP_MODIFICATION_REQUESTS)
                    .count(counter)
                    .build());

            responseTO.getHpModificationRequest().setStatusWiseCount(tempHpModificationRequests);
        }

        /**
         * Data retrieval - Temporary Suspension
         */
        List<String> filteredStatusListForTemporarySuspension = new ArrayList<>();
        responseTO.setTemporarySuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_TEMPORARY_SUSPENSION.getId().toString())
                .build());
        List<StatusWiseCountTO> temporarySuspensionRequests = new ArrayList<>();
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
            temporarySuspensionRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        filteredStatusListForTemporarySuspension.add(statusWiseCountTO.getName());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempTemporarySuspensionRequests = temporarySuspensionRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForTemporarySuspension.contains(workflowStatus.getDescription())) {
                    tempTemporarySuspensionRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .build());
                }
            });

            tempTemporarySuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_TEMPORARY_SUSPENSION_REQUESTS)
                    .count(counter)
                    .build());

            temporarySuspensionRequests=tempTemporarySuspensionRequests;

            responseTO.getTemporarySuspensionRequest().setStatusWiseCount(temporarySuspensionRequests);
        }
        tempCount = counter;

        /**
         * Data retrieval - Permanent Suspension
         */
        List<StatusWiseCountTO> permanentSuspensionRequests = new ArrayList<>();
        List<String> filteredStatusListForPermanentSuspension = new ArrayList<>();
        responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_PERMANENT_SUSPENSION.getId().toString())
                .build());
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
            permanentSuspensionRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_PERMANENT_SUSPENSION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        filteredStatusListForPermanentSuspension.add(statusWiseCountTO.getName());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempPermanentSuspensionRequests = permanentSuspensionRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForPermanentSuspension.contains(workflowStatus.getDescription())) {
                    tempPermanentSuspensionRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .build());
                }
            });

            tempPermanentSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_PERMANENT_SUSPENSION_REQUESTS)
                    .count(counter)
                    .build());

            permanentSuspensionRequests=tempPermanentSuspensionRequests;

            responseTO.getPermanentSuspensionRequest().setStatusWiseCount(permanentSuspensionRequests);
        }

        /**
         * Consolidated Suspension Requests
         */
        List<StatusWiseCountTO> consolidatedSuspensionRequests = new ArrayList<>();
        responseTO.setConsolidatedSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_TEMPORARY_SUSPENSION.getId().toString()+COMMA_SEPARATOR+ApplicationType.HP_PERMANENT_SUSPENSION.getId().toString())
                .build());
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus ->
                    consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .build())
            );

            List<StatusWiseCountTO> tempTemporarySuspensionRequests = temporarySuspensionRequests;
            List<StatusWiseCountTO> tempPermanentSuspensionRequests = permanentSuspensionRequests;
            consolidatedSuspensionRequests.forEach(statusWiseCountTO -> {
                BigInteger tempSuspensionRequestCount = tempTemporarySuspensionRequests.stream()
                        .filter(statusWiseCountTO1 -> statusWiseCountTO1.getName().equals(statusWiseCountTO.getName()))
                        .findFirst()
                        .get()
                        .getCount();
                BigInteger permanentSuspensionRequestCount = tempPermanentSuspensionRequests.stream()
                        .filter(statusWiseCountTO1 -> statusWiseCountTO1.getName().equals(statusWiseCountTO.getName()))
                        .findFirst()
                        .get()
                        .getCount();
                statusWiseCountTO.setCount(tempSuspensionRequestCount.add(permanentSuspensionRequestCount));
            });

            consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS)
                    .count(counter.add(tempCount))
                    .build());

            responseTO.getConsolidatedSuspensionRequest().setStatusWiseCount(consolidatedSuspensionRequests);

        }

        /**
         * Data retrieval - Activate License
         */
        List<StatusWiseCountTO> activateLicenseRequests;
        List<String> filteredStatusListForLicenseRequests = new ArrayList<>();
        responseTO.setActivateLicenseRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_ACTIVATE_LICENSE.getId().toString())
                .build());
        if(Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;

            activateLicenseRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_ACTIVATE_LICENSE.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        filteredStatusListForLicenseRequests.add(statusWiseCountTO.getName());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempActivateLicenseRequests = activateLicenseRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForLicenseRequests.contains(workflowStatus.getDescription())) {
                    tempActivateLicenseRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .build());
                }
            });

            tempActivateLicenseRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_ACTIVATE_LICENSE_REQUESTS)
                    .count(counter)
                    .build());

            activateLicenseRequests = tempActivateLicenseRequests;

            responseTO.getActivateLicenseRequest().setStatusWiseCount(activateLicenseRequests);
        }

        /**
         * Data retrieval - College Registration
         */
        List<StatusWiseCountTO> collegeRegistrationRequests;
        List<String> filteredStatusListForCollegeRegRequests = new ArrayList<>();
        responseTO.setCollegeRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.COLLEGE_REGISTRATION.getId().toString())
                .build());
        if(Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
            collegeRegistrationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.COLLEGE_REGISTRATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        filteredStatusListForCollegeRegRequests.add(statusWiseCountTO.getName());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempCollegeRegistrationRequests = collegeRegistrationRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForCollegeRegRequests.contains(workflowStatus.getDescription())) {
                    tempCollegeRegistrationRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .build());
                }
            });

            tempCollegeRegistrationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_COLLEGE_REGISTRATION_REQUESTS)
                    .count(counter)
                    .build());
            collegeRegistrationRequests = tempCollegeRegistrationRequests;

            responseTO.getCollegeRegistrationRequest().setStatusWiseCount(collegeRegistrationRequests);
        }

        return responseTO;
    }

    private List<StatusWiseCountTO> fetchStatusWiseCountBasedOnLoggedInUser(BigInteger applicationTypeId, User loggedInUser) throws InvalidRequestException {

        UserGroup group=loggedInUser.getGroup();
        BigInteger groupId=group.getId();

        /**
         * Group - College Dean
         */
        if (Group.COLLEGE_DEAN.getDescription().equals(group.getName())){
            BigInteger collegeDeanId=iCollegeDeanRepository.getCollegeDeanIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusWiseCount> indianHpStatusWiseCount=iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(applicationTypeId, groupId, collegeDeanId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount= iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, collegeDeanId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(ApplicationType.FOREIGN_HP_MODIFICATION.getId(), groupId, collegeDeanId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            return indianHpStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).collect(Collectors.toList());
        }

        /**
         * Group - College Admin
         */
        if (Group.COLLEGE_ADMIN.getDescription().equals(group.getName())){
            BigInteger collegeId=iCollegeRepository.getCollegeIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusWiseCount> indianHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForAdmin(applicationTypeId, groupId, collegeId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount= iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForAdmin(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, collegeId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForAdmin(ApplicationType.FOREIGN_HP_MODIFICATION.getId(), groupId, collegeId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            return indianHpStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).collect(Collectors.toList());
        }

        /**
         * Group - College Registrar
         */
        if (Group.COLLEGE_REGISTRAR.getDescription().equals(group.getName())){
            BigInteger collegeRegistrarId=iCollegeRegistrarRepository.getCollegeRegistrarIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusWiseCount> indianHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(applicationTypeId, groupId, collegeRegistrarId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount= iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, collegeRegistrarId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(ApplicationType.FOREIGN_HP_MODIFICATION.getId(), groupId, collegeRegistrarId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            return indianHpStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).collect(Collectors.toList());
        }

        /**
         * Group - State Medical council
         */
        if (Group.SMC.getDescription().equals(group.getName())){
            BigInteger smcProfileId=iSmcProfileRepository.getSmcIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusWiseCount> indianHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(applicationTypeId, groupId, smcProfileId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount= iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, smcProfileId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(ApplicationType.FOREIGN_HP_MODIFICATION.getId(), groupId, smcProfileId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            return indianHpStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).collect(Collectors.toList());
        }

        /**
         * Group - National Medical council
         */
        if (Group.NMC.getDescription().equals(group.getName())) {
            List<IStatusWiseCount> indianHpStatusWiseCount = iFetchCountOnCardRepository.fetchStatusWiseCount(applicationTypeId,groupId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount= iFetchCountOnCardRepository.fetchStatusWiseCount(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchStatusWiseCount(ApplicationType.FOREIGN_HP_MODIFICATION.getId(), groupId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            return indianHpStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).collect(Collectors.toList());
        }

        /**
         * Group - NBE
         */
        if (Group.NBE.getDescription().equals(group.getName())) {
            BigInteger nbeProfileId=iNbeProfileRepository.getNbeProfileIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusWiseCount> indianHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForNbe(applicationTypeId,groupId,nbeProfileId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount= iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForNbe(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, nbeProfileId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)){
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForNbe(ApplicationType.FOREIGN_HP_MODIFICATION.getId(), groupId, nbeProfileId);
                return fetchCountConsideringForeignUsers(indianHpStatusWiseCount,foreignHpStatusWiseCount);
            }
            return indianHpStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).collect(Collectors.toList());
        }

        throw new InvalidRequestException(INVALID_DASHBOARD_GROUP);
    }

    private List<StatusWiseCountTO> fetchCountConsideringForeignUsers(List<IStatusWiseCount> indianHpStatusWiseCount, List<IStatusWiseCount> foreignHpStatusWiseCount){
        List<StatusWiseCountTO> tempForeignHpStatusWiseCount=foreignHpStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).collect(Collectors.toList());
        return indianHpStatusWiseCount.stream().map(iStatusWiseCount -> {
            StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount);
            statusWiseCountTO.setCount(statusWiseCountTO.getCount().add(
                    tempForeignHpStatusWiseCount.stream()
                            .filter(iStatusWiseCount1-> statusWiseCountTO.getName().equals(iStatusWiseCount1.getName()))
                            .findFirst()
                            .orElse(StatusWiseCountTO.builder().count(BigInteger.ZERO).build())
                            .getCount()));
            return statusWiseCountTO;
        }).collect(Collectors.toList());
    }


}
