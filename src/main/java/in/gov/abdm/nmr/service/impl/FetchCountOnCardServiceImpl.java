package in.gov.abdm.nmr.service.impl;

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
    private ISmcProfileRepository iSmcProfileRepository;

    private BigInteger counter=BigInteger.ZERO;

    private BigInteger tempCount=BigInteger.ZERO;

    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard() throws InvalidRequestException, AccessDeniedException {
        User loggedInUser = accessControlService.getLoggedInUser();
        String groupName = loggedInUser.getGroup().getName();
        if (loggedInUser == null) {
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
        }

        /**
         * Data retrieval - HP Registration
         */
        List<String> filteredStatusListForRegistration = new ArrayList<>();
        List<StatusWiseCountTO> hpRegistrationRequests = new ArrayList<>();
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE_ADMIN.getDescription().equals(groupName) || Group.COLLEGE_DEAN.getDescription().equals(groupName) || Group.COLLEGE_REGISTRAR.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter=BigInteger.ZERO;
            hpRegistrationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_REGISTRATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCount -> {
                        counter = counter.add(statusWiseCount.getCount());
                        filteredStatusListForRegistration.add(statusWiseCount.getName());
                        StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                        statusWiseCountTO.setApplicationTypeId(ApplicationType.HP_REGISTRATION.getId());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempHpRegistrationRequests = hpRegistrationRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForRegistration.contains(workflowStatus.getDescription())) {
                    tempHpRegistrationRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .applicationTypeId(ApplicationType.HP_REGISTRATION.getId())
                            .build());
                }
            });

            tempHpRegistrationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_HP_REGISTRATION_REQUESTS)
                    .count(counter)
                    .applicationTypeId(ApplicationType.HP_REGISTRATION.getId())
                    .build());

            hpRegistrationRequests=tempHpRegistrationRequests;
        }

        /**
         * Data retrieval - HP Modification
         */
        List<String> filteredStatusListForModification = new ArrayList<>();
        List<StatusWiseCountTO> hpModificationRequests = new ArrayList<>();
        counter = BigInteger.ZERO;
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE_ADMIN.getDescription().equals(groupName) || Group.COLLEGE_DEAN.getDescription().equals(groupName) || Group.COLLEGE_REGISTRAR.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName) || Group.NBE.getDescription().equals(groupName)) {

        hpModificationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_MODIFICATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCount -> {
                        counter = counter.add(statusWiseCount.getCount());
                        filteredStatusListForModification.add(statusWiseCount.getName());
                        StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                        statusWiseCountTO.setApplicationTypeId(ApplicationType.HP_MODIFICATION.getId());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempHpModificationRequests = hpModificationRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForModification.contains(workflowStatus.getDescription())) {
                    tempHpModificationRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .applicationTypeId(ApplicationType.HP_MODIFICATION.getId())
                            .build());
                }
            });

            tempHpModificationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_HP_MODIFICATION_REQUESTS)
                    .count(counter)
                    .applicationTypeId(ApplicationType.HP_MODIFICATION.getId())
                    .build());
            hpModificationRequests=tempHpModificationRequests;
        }

        /**
         * Data retrieval - Temporary Suspension
         */
        List<String> filteredStatusListForTemporarySuspension = new ArrayList<>();
        List<StatusWiseCountTO> temporarySuspensionRequests = new ArrayList<>();
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
        temporarySuspensionRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCount -> {
                        counter = counter.add(statusWiseCount.getCount());
                        filteredStatusListForTemporarySuspension.add(statusWiseCount.getName());
                        StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                        statusWiseCountTO.setApplicationTypeId(ApplicationType.HP_TEMPORARY_SUSPENSION.getId());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempTemporarySuspensionRequests = temporarySuspensionRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForTemporarySuspension.contains(workflowStatus.getDescription())) {
                    tempTemporarySuspensionRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .applicationTypeId(ApplicationType.HP_TEMPORARY_SUSPENSION.getId())
                            .build());
                }
            });

            tempTemporarySuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_TEMPORARY_SUSPENSION_REQUESTS)
                    .count(counter)
                    .applicationTypeId(ApplicationType.HP_TEMPORARY_SUSPENSION.getId())
                    .build());

            temporarySuspensionRequests=tempTemporarySuspensionRequests;
        }
        tempCount = counter;

        /**
         * Data retrieval - Permanent Suspension
         */
        List<StatusWiseCountTO> permanentSuspensionRequests = new ArrayList<>();
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            List<String> filteredStatusListForPermanentSuspension = new ArrayList<>();
            counter = BigInteger.ZERO;
            permanentSuspensionRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_PERMANENT_SUSPENSION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCount -> {
                        counter = counter.add(statusWiseCount.getCount());
                        filteredStatusListForPermanentSuspension.add(statusWiseCount.getName());
                        StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                        statusWiseCountTO.setApplicationTypeId(ApplicationType.HP_PERMANENT_SUSPENSION.getId());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempPermanentSuspensionRequests = permanentSuspensionRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForPermanentSuspension.contains(workflowStatus.getDescription())) {
                    tempPermanentSuspensionRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .applicationTypeId(ApplicationType.HP_PERMANENT_SUSPENSION.getId())
                            .build());
                }
            });

            tempPermanentSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_PERMANENT_SUSPENSION_REQUESTS)
                    .count(counter)
                    .applicationTypeId(ApplicationType.HP_PERMANENT_SUSPENSION.getId())
                    .build());

            permanentSuspensionRequests=tempPermanentSuspensionRequests;
        }

        /**
         * Consolidated Suspension Requests
         */
        List<StatusWiseCountTO> consolidatedSuspensionRequests = new ArrayList<>();
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

        }

        /**
         * Data retrieval - Activate License
         */
        List<StatusWiseCountTO> activateLicenseRequests = new ArrayList<>();
        if(Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            List<String> filteredStatusListForLicenseRequests = new ArrayList<>();
            counter = BigInteger.ZERO;
            activateLicenseRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_ACTIVATE_LICENSE.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCount -> {
                        counter = counter.add(statusWiseCount.getCount());
                        filteredStatusListForLicenseRequests.add(statusWiseCount.getName());
                        StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                        statusWiseCountTO.setApplicationTypeId(ApplicationType.HP_ACTIVATE_LICENSE.getId());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempActivateLicenseRequests = activateLicenseRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForLicenseRequests.contains(workflowStatus.getDescription())) {
                    tempActivateLicenseRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .applicationTypeId(ApplicationType.HP_ACTIVATE_LICENSE.getId())
                            .build());
                }
            });

            tempActivateLicenseRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_ACTIVATE_LICENSE_REQUESTS)
                    .count(counter)
                    .applicationTypeId(ApplicationType.HP_ACTIVATE_LICENSE.getId())
                    .build());

            activateLicenseRequests = tempActivateLicenseRequests;
        }

        /**
         * Data retrieval - College Registration
         */
        List<StatusWiseCountTO> collegeRegistrationRequests = new ArrayList<>();
        if(Group.NMC.getDescription().equals(groupName)) {
            List<String> filteredStatusListForCollegeRegRequests = new ArrayList<>();
            counter = BigInteger.ZERO;
            collegeRegistrationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.COLLEGE_REGISTRATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCount -> {
                        counter = counter.add(statusWiseCount.getCount());
                        filteredStatusListForCollegeRegRequests.add(statusWiseCount.getName());
                        StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                        statusWiseCountTO.setApplicationTypeId(ApplicationType.COLLEGE_REGISTRATION.getId());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempCollegeRegistrationRequests = collegeRegistrationRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForCollegeRegRequests.contains(workflowStatus.getDescription())) {
                    tempCollegeRegistrationRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .applicationTypeId(ApplicationType.COLLEGE_REGISTRATION.getId())
                            .build());
                }
            });

            tempCollegeRegistrationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_COLLEGE_REGISTRATION_REQUESTS)
                    .count(counter)
                    .applicationTypeId(ApplicationType.COLLEGE_REGISTRATION.getId())
                    .build());
            collegeRegistrationRequests = tempCollegeRegistrationRequests;
        }

        /**
         * Data retrieval - Foreign HP Registration
         */
        List<String> filteredStatusListForForeignHpRegistration = new ArrayList<>();
        List<StatusWiseCountTO> foreignHpRegistrationRequests = new ArrayList<>();
        if (Group.SMC.getDescription().equals(groupName) || Group.NBE.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName) ) {
            counter = BigInteger.ZERO;
            foreignHpRegistrationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_REGISTRATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCount -> {
                        counter = counter.add(statusWiseCount.getCount());
                        filteredStatusListForForeignHpRegistration.add(statusWiseCount.getName());
                        StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                        statusWiseCountTO.setApplicationTypeId(ApplicationType.HP_REGISTRATION.getId());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempForeignHpRegistrationRequests = foreignHpRegistrationRequests;
            Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
                if (!filteredStatusListForForeignHpRegistration.contains(workflowStatus.getDescription())) {
                    tempForeignHpRegistrationRequests.add(StatusWiseCountTO.builder()
                            .name(workflowStatus.getDescription())
                            .count(BigInteger.ZERO)
                            .applicationTypeId(ApplicationType.FOREIGN_HP_REGISTRATION.getId())
                            .build());
                }
            });

            tempForeignHpRegistrationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_FOREIGN_HP_REGISTRATION_REQUESTS)
                    .count(counter)
                    .applicationTypeId(ApplicationType.FOREIGN_HP_REGISTRATION.getId())
                    .build());

            foreignHpRegistrationRequests=tempForeignHpRegistrationRequests;
        }

        return FetchCountOnCardResponseTO.builder()
                .hpRegistrationRequests(hpRegistrationRequests)
                .hpModificationRequests(hpModificationRequests)
                .temporarySuspensionRequests(temporarySuspensionRequests)
                .permanentSuspensionRequests(permanentSuspensionRequests)
                .consolidatedSuspensionRequests(consolidatedSuspensionRequests)
                .activateLicenseRequests(activateLicenseRequests)
                .collegeRegistrationRequests(collegeRegistrationRequests)
                .foreignHpRegistrationRequests(foreignHpRegistrationRequests)
                .build();
    }

    private List<IStatusWiseCount> fetchStatusWiseCountBasedOnLoggedInUser(BigInteger applicationTypeId, User loggedInUser) throws InvalidRequestException {

        UserGroup group=loggedInUser.getGroup();
        BigInteger groupId=group.getId();
        /**
         * Group - Health Professional
         */
        if (Group.HEALTH_PROFESSIONAL.getDescription().equals(group.getName())){
            BigInteger hpProfileId=iHpProfileRepository.getHpProfileIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForHp(applicationTypeId, groupId, hpProfileId);
        }

        /**
         * Group - College Dean
         */
        if (Group.COLLEGE_DEAN.getDescription().equals(group.getName())){
            BigInteger collegeDeanId=iCollegeDeanRepository.getCollegeDeanIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(applicationTypeId, groupId, collegeDeanId);
        }

        /**
         * Group - College Registrar
         */
        if (Group.COLLEGE_REGISTRAR.getDescription().equals(group.getName())){
            BigInteger collegeRegistrarId=iCollegeRegistrarRepository.getCollegeRegistrarIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(applicationTypeId, groupId, collegeRegistrarId);
        }

        /**
         * Group - State Medical council
         */
        if (Group.SMC.getDescription().equals(group.getName())){
            BigInteger smcProfileId=iSmcProfileRepository.getSmcIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(applicationTypeId, groupId, smcProfileId);
        }

        /**
         * Group - National Medical council or NBE
         */
        if (Group.NMC.getDescription().equals(group.getName()) || Group.NBE.getDescription().equals(group.getName())) {
            return iFetchCountOnCardRepository.fetchStatusWiseCount(applicationTypeId,groupId);
        }

        throw new InvalidRequestException(INVALID_GROUP);
    }

}
