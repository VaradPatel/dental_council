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

    private BigInteger counter = BigInteger.ZERO;

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
        responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_REGISTRATION.getId().toString() + COMMA_SEPARATOR + ApplicationType.FOREIGN_HP_REGISTRATION.getId().toString())
                .build());
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE_ADMIN.getDescription().equals(groupName) || Group.COLLEGE_DEAN.getDescription().equals(groupName) || Group.COLLEGE_REGISTRAR.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName) || Group.NBE.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
            List<StatusWiseCountTO> hpRegistrationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_REGISTRATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        return statusWiseCountTO;
                    }).collect(Collectors.toList());

            List<StatusWiseCountTO> tempHpRegistrationRequests = new ArrayList<>();

            tempHpRegistrationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_HP_REGISTRATION_REQUESTS)
                    .count(counter)
                    .build());

            tempHpRegistrationRequests.add(hpRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpRegistrationRequests.add(hpRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build()));

            tempHpRegistrationRequests.add(hpRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.QUERY_RAISED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpRegistrationRequests.add(hpRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.REJECTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpRegistrationRequests.add(hpRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.SUSPENDED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpRegistrationRequests.add(hpRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.BLACKLISTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build()));

            responseTO.getHpRegistrationRequest().setStatusWiseCount(tempHpRegistrationRequests);
        }

        /**
         * Data retrieval - HP Modification
         */
        responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_MODIFICATION.getId().toString() + COMMA_SEPARATOR + ApplicationType.QUALIFICATION_ADDITION.getId().toString())
                .build());
        counter = BigInteger.ZERO;
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE_ADMIN.getDescription().equals(groupName) || Group.COLLEGE_DEAN.getDescription().equals(groupName) || Group.COLLEGE_REGISTRAR.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName) || Group.NBE.getDescription().equals(groupName)) {
            List<StatusWiseCountTO> hpModificationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_MODIFICATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempHpModificationRequests = new ArrayList<>();

            tempHpModificationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_HP_MODIFICATION_REQUESTS)
                    .count(counter)
                    .build());

            tempHpModificationRequests.add(hpModificationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpModificationRequests.add(hpModificationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build()));

            tempHpModificationRequests.add(hpModificationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.QUERY_RAISED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpModificationRequests.add(hpModificationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.REJECTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpModificationRequests.add(hpModificationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.SUSPENDED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build()));

            tempHpModificationRequests.add(hpModificationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.BLACKLISTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build()));

            responseTO.getHpModificationRequest().setStatusWiseCount(tempHpModificationRequests);
        }

        /**
         * Data retrieval - Temporary Suspension
         */
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
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempTemporarySuspensionRequests = new ArrayList<>();

            tempTemporarySuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_TEMPORARY_SUSPENSION_REQUESTS)
                    .count(counter)
                    .build());

            tempTemporarySuspensionRequests.add(temporarySuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build()));

            tempTemporarySuspensionRequests.add(temporarySuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build()));

            tempTemporarySuspensionRequests.add(temporarySuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.QUERY_RAISED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build()));

            tempTemporarySuspensionRequests.add(temporarySuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.REJECTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build()));

            tempTemporarySuspensionRequests.add(temporarySuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.SUSPENDED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build()));

            tempTemporarySuspensionRequests.add(temporarySuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.BLACKLISTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build()));


            responseTO.getTemporarySuspensionRequest().setStatusWiseCount(tempTemporarySuspensionRequests);
        }

        /**
         * Data retrieval - Permanent Suspension
         */
        List<StatusWiseCountTO> permanentSuspensionRequests = new ArrayList<>();
        responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_PERMANENT_SUSPENSION.getId().toString())
                .build());
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
            permanentSuspensionRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_PERMANENT_SUSPENSION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempPermanentSuspensionRequests = new ArrayList<>();

            tempPermanentSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_PERMANENT_SUSPENSION_REQUESTS)
                    .count(counter)
                    .build());

            tempPermanentSuspensionRequests.add(permanentSuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build()));

            tempPermanentSuspensionRequests.add(permanentSuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build()));

            tempPermanentSuspensionRequests.add(permanentSuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.QUERY_RAISED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build()));

            tempPermanentSuspensionRequests.add(permanentSuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.REJECTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build()));

            tempPermanentSuspensionRequests.add(permanentSuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.SUSPENDED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build()));

            tempPermanentSuspensionRequests.add(permanentSuspensionRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.BLACKLISTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build()));


            responseTO.getPermanentSuspensionRequest().setStatusWiseCount(tempPermanentSuspensionRequests);
        }

        /**
         * Consolidated Suspension Requests
         */
        List<StatusWiseCountTO> consolidatedSuspensionRequests = new ArrayList<>();
        responseTO.setConsolidatedSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_TEMPORARY_SUSPENSION.getId().toString() + COMMA_SEPARATOR + ApplicationType.HP_PERMANENT_SUSPENSION.getId().toString())
                .build());
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
            consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(CONSOLIDATED_PENDING_TEMPORARY_SUSPENSION_REQUESTS)
                    .count(responseTO.getTemporarySuspensionRequest()
                            .getStatusWiseCount().stream()
                            .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                            .findFirst()
                            .orElse(StatusWiseCountTO.builder().count(BigInteger.ZERO).build())
                            .getCount())
                    .build());

            consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(CONSOLIDATED_APPROVED_TEMPORARY_SUSPENSION_REQUESTS)
                    .count(responseTO.getTemporarySuspensionRequest()
                            .getStatusWiseCount().stream()
                            .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                            .findFirst()
                            .orElse(StatusWiseCountTO.builder().count(BigInteger.ZERO).build())
                            .getCount())
                    .build());

            consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(CONSOLIDATED_PENDING_PERMANENT_SUSPENSION_REQUESTS)
                    .count(responseTO.getPermanentSuspensionRequest()
                            .getStatusWiseCount().stream()
                            .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                            .findFirst()
                            .orElse(StatusWiseCountTO.builder().count(BigInteger.ZERO).build())
                            .getCount())
                    .build());

            consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(CONSOLIDATED_APPROVED_PERMANENT_SUSPENSION_REQUESTS)
                    .count(responseTO.getPermanentSuspensionRequest()
                            .getStatusWiseCount().stream()
                            .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                            .findFirst()
                            .orElse(StatusWiseCountTO.builder().count(BigInteger.ZERO).build())
                            .getCount())
                    .build());

            List<StatusWiseCountTO> tempConsolidatedSuspensionRequests = new ArrayList<>();

            consolidatedSuspensionRequests.stream().forEach(statusWiseCountTO -> counter = counter.add(statusWiseCountTO.getCount()));

            tempConsolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS)
                    .count(counter)
                    .build());

            tempConsolidatedSuspensionRequests.addAll(consolidatedSuspensionRequests);

            responseTO.getConsolidatedSuspensionRequest().setStatusWiseCount(tempConsolidatedSuspensionRequests);

        }

        /**
         * Data retrieval - Activate License
         */
        List<StatusWiseCountTO> activateLicenseRequests;
        responseTO.setActivateLicenseRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.HP_ACTIVATE_LICENSE.getId().toString())
                .build());
        if (Group.SMC.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;

            activateLicenseRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_ACTIVATE_LICENSE.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempActivateLicenseRequests = new ArrayList<>();

            tempActivateLicenseRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_ACTIVATE_LICENSE_REQUESTS)
                    .count(counter)
                    .build());

            tempActivateLicenseRequests.add(activateLicenseRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build()));

            tempActivateLicenseRequests.add(activateLicenseRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build()));

            tempActivateLicenseRequests.add(activateLicenseRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.QUERY_RAISED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build()));

            tempActivateLicenseRequests.add(activateLicenseRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.REJECTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build()));

            tempActivateLicenseRequests.add(activateLicenseRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.SUSPENDED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build()));

            tempActivateLicenseRequests.add(activateLicenseRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.BLACKLISTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build()));


            responseTO.getActivateLicenseRequest().setStatusWiseCount(tempActivateLicenseRequests);
        }

        /**
         * Data retrieval - College Registration
         */
        List<StatusWiseCountTO> collegeRegistrationRequests;
        responseTO.setCollegeRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(ApplicationType.COLLEGE_REGISTRATION.getId().toString())
                .build());
        if (Group.NMC.getDescription().equals(groupName)) {
            counter = BigInteger.ZERO;
            collegeRegistrationRequests = fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.COLLEGE_REGISTRATION.getId(), loggedInUser)
                    .stream()
                    .map(statusWiseCountTO -> {
                        counter = counter.add(statusWiseCountTO.getCount());
                        return statusWiseCountTO;
                    })
                    .collect(Collectors.toList());

            List<StatusWiseCountTO> tempCollegeRegistrationRequests = new ArrayList<>();

            tempCollegeRegistrationRequests.add(StatusWiseCountTO.builder()
                    .name(TOTAL_COLLEGE_REGISTRATION_REQUESTS)
                    .count(counter)
                    .build());

            tempCollegeRegistrationRequests.add(collegeRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.APPROVED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build()));

            tempCollegeRegistrationRequests.add(collegeRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.PENDING.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build()));

            tempCollegeRegistrationRequests.add(collegeRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.QUERY_RAISED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build()));

            tempCollegeRegistrationRequests.add(collegeRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.REJECTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build()));

            tempCollegeRegistrationRequests.add(collegeRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.SUSPENDED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build()));

            tempCollegeRegistrationRequests.add(collegeRegistrationRequests.stream()
                    .filter(statusWiseCountTO -> WorkflowStatus.BLACKLISTED.getDescription().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .orElse(StatusWiseCountTO.builder().name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build()));


            responseTO.getCollegeRegistrationRequest().setStatusWiseCount(tempCollegeRegistrationRequests);
        }

        return responseTO;
    }

    private List<StatusWiseCountTO> fetchStatusWiseCountBasedOnLoggedInUser(BigInteger applicationTypeId, User loggedInUser) throws InvalidRequestException {

        UserGroup group = loggedInUser.getGroup();
        BigInteger groupId = group.getId();

        /**
         * Group - College Dean
         */
        if (Group.COLLEGE_DEAN.getDescription().equals(group.getName())) {
            BigInteger collegeId = iCollegeDeanRepository.findByUserId(loggedInUser.getId()).getCollege().getId();
            List<IStatusWiseCount> primaryStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(applicationTypeId, groupId, collegeId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, collegeId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, foreignHpStatusWiseCount);
            } else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> qualificationAdditionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(ApplicationType.QUALIFICATION_ADDITION.getId(), groupId, collegeId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, qualificationAdditionStatusWiseCount);
            }
            return primaryStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).toList();
        }

        /**
         * Group - College Admin
         */
        if (Group.COLLEGE_ADMIN.getDescription().equals(group.getName())) {
            BigInteger collegeId = iCollegeRepository.getCollegeIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusWiseCount> primaryStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForAdmin(applicationTypeId, groupId, collegeId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForAdmin(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, collegeId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, foreignHpStatusWiseCount);
            } else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> qualificationAdditionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForAdmin(ApplicationType.QUALIFICATION_ADDITION.getId(), groupId, collegeId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, qualificationAdditionStatusWiseCount);
            }
            return primaryStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).toList();
        }

        /**
         * Group - College Registrar
         */
        if (Group.COLLEGE_REGISTRAR.getDescription().equals(group.getName())) {
            BigInteger collegeId = iCollegeRegistrarRepository.findByUserId(loggedInUser.getId()).getCollege().getId();
            List<IStatusWiseCount> primaryStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(applicationTypeId, groupId, collegeId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, collegeId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, foreignHpStatusWiseCount);
            } else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> qualificationAdditionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(ApplicationType.QUALIFICATION_ADDITION.getId(), groupId, collegeId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, qualificationAdditionStatusWiseCount);
            }
            return primaryStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).toList();
        }

        /**
         * Group - State Medical council
         */
        if (Group.SMC.getDescription().equals(group.getName())) {
            BigInteger smcProfileId = iSmcProfileRepository.getSmcIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusWiseCount> primaryStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(applicationTypeId, groupId, smcProfileId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId, smcProfileId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, foreignHpStatusWiseCount);
            } else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> qualificationAdditionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(ApplicationType.QUALIFICATION_ADDITION.getId(), groupId, smcProfileId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, qualificationAdditionStatusWiseCount);
            }
            else if (ApplicationType.HP_TEMPORARY_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> temporarySuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForSmc(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), smcProfileId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, temporarySuspensionStatusWiseCount);
            }
            else if (ApplicationType.HP_PERMANENT_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> permanentSuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForSmc(ApplicationType.HP_PERMANENT_SUSPENSION.getId(), smcProfileId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, permanentSuspensionStatusWiseCount);
            }
            else if (ApplicationType.HP_ACTIVATE_LICENSE.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> activateLicenseStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForSmc(ApplicationType.HP_ACTIVATE_LICENSE.getId(), smcProfileId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, activateLicenseStatusWiseCount);
            }
            return primaryStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).toList();
        }

        /**
         * Group - National Medical council
         */
        if (Group.NMC.getDescription().equals(group.getName())) {
            List<IStatusWiseCount> primaryStatusWiseCount = iFetchCountOnCardRepository.fetchStatusWiseCount(applicationTypeId, groupId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchStatusWiseCount(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, foreignHpStatusWiseCount);
            } else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> qualificationAdditionStatusWiseCount = iFetchCountOnCardRepository.fetchStatusWiseCount(ApplicationType.QUALIFICATION_ADDITION.getId(), groupId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, qualificationAdditionStatusWiseCount);
            }
            else if (ApplicationType.HP_TEMPORARY_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> temporarySuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForNmc(ApplicationType.HP_TEMPORARY_SUSPENSION.getId());
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, temporarySuspensionStatusWiseCount);
            }
            else if (ApplicationType.HP_PERMANENT_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> permanentSuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForNmc(ApplicationType.HP_PERMANENT_SUSPENSION.getId());
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, permanentSuspensionStatusWiseCount);
            }
            else if (ApplicationType.HP_ACTIVATE_LICENSE.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> activateLicenseStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForNmc(ApplicationType.HP_ACTIVATE_LICENSE.getId());
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, activateLicenseStatusWiseCount);
            }
            return primaryStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).toList();
        }

        /**
         * Group - NBE
         */
        if (Group.NBE.getDescription().equals(group.getName())) {
            List<IStatusWiseCount> primaryStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForNbe(applicationTypeId, groupId);

            if (ApplicationType.HP_REGISTRATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> foreignHpStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForNbe(ApplicationType.FOREIGN_HP_REGISTRATION.getId(), groupId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, foreignHpStatusWiseCount);
            } else if (ApplicationType.HP_MODIFICATION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> qualificationAdditionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForNbe(ApplicationType.QUALIFICATION_ADDITION.getId(), groupId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, qualificationAdditionStatusWiseCount);
            }
            return primaryStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).toList();
        }

        throw new InvalidRequestException(INVALID_DASHBOARD_GROUP);
    }

    private List<StatusWiseCountTO> fetchCountConsideringCompositeApplicationTypes(List<IStatusWiseCount> primaryStatusWiseCount, List<IStatusWiseCount> secondaryStatusWiseCount) {
        List<StatusWiseCountTO> tempSecondaryStatusWiseCountTO = secondaryStatusWiseCount.stream().map(iStatusWiseCount -> iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount)).toList();
        return primaryStatusWiseCount.stream().map(iStatusWiseCount -> {
            StatusWiseCountTO statusWiseCountTO = iStatusWiseCountMapper.toStatusWiseCountTO(iStatusWiseCount);
            statusWiseCountTO.setCount(statusWiseCountTO.getCount().add(
                    tempSecondaryStatusWiseCountTO.stream()
                            .filter(iStatusWiseCount1 -> statusWiseCountTO.getName().equals(iStatusWiseCount1.getName()))
                            .findFirst()
                            .orElse(StatusWiseCountTO.builder().count(BigInteger.ZERO).build())
                            .getCount()));
            return statusWiseCountTO;
        }).collect(Collectors.toList());
    }


}
