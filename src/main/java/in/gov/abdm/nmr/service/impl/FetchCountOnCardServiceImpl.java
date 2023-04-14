package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchCountOnCardInnerResponseTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IStatusCount;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserGroup;
import in.gov.abdm.nmr.repository.IFetchCountOnCardRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.mapper.IStatusWiseCount;
import in.gov.abdm.nmr.mapper.IStatusWiseCountMapper;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.*;
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
    private ISmcProfileRepository iSmcProfileRepository;

    @Autowired
    private INbeProfileRepository iNbeProfileRepository;

    @Autowired
    ICollegeProfileDaoService iCollegeProfileDaoService;

    private BigInteger counter = BigInteger.ZERO;

    private static final Map<String, List<BigInteger>> applicationIds =
            Map.of("hp_registration_request", List.of(ApplicationType.HP_REGISTRATION.getId(), ApplicationType.FOREIGN_HP_REGISTRATION.getId()),
                    "hp_modification_request", List.of(ApplicationType.HP_MODIFICATION.getId(), ApplicationType.QUALIFICATION_ADDITION.getId()),
                    "temporary_suspension_request", List.of(ApplicationType.HP_TEMPORARY_SUSPENSION.getId()),
                    "permanent_suspension_request", List.of(ApplicationType.HP_PERMANENT_SUSPENSION.getId()));

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
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName) || Group.NBE.getDescription().equals(groupName)) {
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
        if (Group.SMC.getDescription().equals(groupName) || Group.COLLEGE.getDescription().equals(groupName) || Group.NMC.getDescription().equals(groupName) || Group.NBE.getDescription().equals(groupName)) {
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

    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard1() throws AccessDeniedException {
        User loggedInUser = accessControlService.getLoggedInUser();
        String groupName = loggedInUser.getGroup().getName();
        if (loggedInUser == null) {
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
        }
        FetchCountOnCardResponseTO responseTO = new FetchCountOnCardResponseTO();
        BigInteger totalCount = BigInteger.ZERO;
        List<BigInteger> hpProfileStatuses = Arrays.stream(HpProfileStatus.values()).map(s -> s.getId()).collect(Collectors.toList());

        if (Group.SMC.getDescription().equals(groupName)) {
            BigInteger smcProfileId = iSmcProfileRepository.getSmcIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForSmc(smcProfileId);

            final List<StatusWiseCountTO> hpRegistration = getDefault();
            responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_registration_request").toString()).build());
            responseTO.getHpRegistrationRequest().setStatusWiseCount(getCardResponse(
                    hpRegistration, totalCount, statusCounts, hpProfileStatuses, "hp_registration_request",
                    TOTAL_HP_REGISTRATION_REQUESTS));

            final List<StatusWiseCountTO> hpModification = getDefault();
            responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_modification_request").toString()).build());
            responseTO.getHpModificationRequest().setStatusWiseCount(getCardResponse(
                    hpModification, totalCount, statusCounts, hpProfileStatuses, "hp_modification_request",
                    TOTAL_HP_MODIFICATION_REQUESTS));

            final List<StatusWiseCountTO> temporarySuspension = getDefault();
            responseTO.setTemporarySuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("temporary_suspension_request").toString()).build());
            responseTO.getTemporarySuspensionRequest().setStatusWiseCount(getCardResponse(
                    temporarySuspension, totalCount, statusCounts, hpProfileStatuses, "temporary_suspension_request",
                    TOTAL_TEMPORARY_SUSPENSION_REQUESTS));

            final List<StatusWiseCountTO> permanentSuspension = getDefault();
            responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("permanent_suspension_request").toString()).build());
            responseTO.getPermanentSuspensionRequest().setStatusWiseCount(getCardResponse(
                    permanentSuspension, totalCount, statusCounts, hpProfileStatuses, "permanent_suspension_request",
                    TOTAL_PERMANENT_SUSPENSION_REQUESTS));
        }

        if (Group.COLLEGE.getDescription().equals(groupName)) {
            BigInteger collegeId = iCollegeProfileDaoService.findByUserId(loggedInUser.getId()).getCollege().getId();

            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForCollege(collegeId);

            final List<StatusWiseCountTO> hpRegistration = getDefault();
            responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_registration_request").toString()).build());
            responseTO.getHpRegistrationRequest().setStatusWiseCount(getCardResponse(
                    hpRegistration, totalCount, statusCounts, hpProfileStatuses, "hp_registration_request",
                    TOTAL_HP_REGISTRATION_REQUESTS));

            final List<StatusWiseCountTO> hpModification = getDefault();
            responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_modification_request").toString()).build());
            responseTO.getHpModificationRequest().setStatusWiseCount(getCardResponse(
                    hpModification, totalCount, statusCounts, hpProfileStatuses, "hp_modification_request",
                    TOTAL_HP_MODIFICATION_REQUESTS));

            final List<StatusWiseCountTO> temporarySuspension = getDefault();
            responseTO.setTemporarySuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("temporary_suspension_request").toString()).build());
            responseTO.getTemporarySuspensionRequest().setStatusWiseCount(getCardResponse(
                    temporarySuspension, totalCount, statusCounts, hpProfileStatuses, "temporary_suspension_request",
                    TOTAL_TEMPORARY_SUSPENSION_REQUESTS));

            final List<StatusWiseCountTO> permanentSuspension = getDefault();
            responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("permanent_suspension_request").toString()).build());
            responseTO.getPermanentSuspensionRequest().setStatusWiseCount(getCardResponse(
                    permanentSuspension, totalCount, statusCounts, hpProfileStatuses, "permanent_suspension_request",
                    TOTAL_PERMANENT_SUSPENSION_REQUESTS));
        }

        if (Group.NMC.getDescription().equals(groupName)) {
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForNmc();

            final List<StatusWiseCountTO> hpRegistration = getDefault();
            responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_registration_request").toString()).build());
            responseTO.getHpRegistrationRequest().setStatusWiseCount(getCardResponse(
                    hpRegistration, totalCount, statusCounts, hpProfileStatuses, "hp_registration_request",
                    TOTAL_HP_REGISTRATION_REQUESTS));

            final List<StatusWiseCountTO> hpModification = getDefault();
            responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_modification_request").toString()).build());
            responseTO.getHpModificationRequest().setStatusWiseCount(getCardResponse(
                    hpModification, totalCount, statusCounts, hpProfileStatuses, "hp_modification_request",
                    TOTAL_HP_MODIFICATION_REQUESTS));

            final List<StatusWiseCountTO> temporarySuspension = getDefault();
            responseTO.setTemporarySuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("temporary_suspension_request").toString()).build());
            responseTO.getTemporarySuspensionRequest().setStatusWiseCount(getCardResponse(
                    temporarySuspension, totalCount, statusCounts, hpProfileStatuses, "temporary_suspension_request",
                    TOTAL_TEMPORARY_SUSPENSION_REQUESTS));

            final List<StatusWiseCountTO> permanentSuspension = getDefault();
            responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("permanent_suspension_request").toString()).build());
            responseTO.getPermanentSuspensionRequest().setStatusWiseCount(getCardResponse(
                    permanentSuspension, totalCount, statusCounts, hpProfileStatuses, "permanent_suspension_request",
                    TOTAL_PERMANENT_SUSPENSION_REQUESTS));
        }

        if (Group.NBE.getDescription().equals(groupName)) {
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForNbe();

            final List<StatusWiseCountTO> hpRegistration = getDefault();
            responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_registration_request").toString()).build());
            responseTO.getHpRegistrationRequest().setStatusWiseCount(getCardResponse(
                    hpRegistration, totalCount, statusCounts, hpProfileStatuses, "hp_registration_request",
                    TOTAL_HP_REGISTRATION_REQUESTS));

            final List<StatusWiseCountTO> hpModification = getDefault();
            responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("hp_modification_request").toString()).build());
            responseTO.getHpModificationRequest().setStatusWiseCount(getCardResponse(
                    hpModification, totalCount, statusCounts, hpProfileStatuses, "hp_modification_request",
                    TOTAL_HP_MODIFICATION_REQUESTS));

            final List<StatusWiseCountTO> temporarySuspension = getDefault();
            responseTO.setTemporarySuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("temporary_suspension_request").toString()).build());
            responseTO.getTemporarySuspensionRequest().setStatusWiseCount(getCardResponse(
                    temporarySuspension, totalCount, statusCounts, hpProfileStatuses, "temporary_suspension_request",
                    TOTAL_TEMPORARY_SUSPENSION_REQUESTS));

            final List<StatusWiseCountTO> permanentSuspension = getDefault();
            responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                    .applicationTypeIds(applicationIds.get("permanent_suspension_request").toString()).build());
            responseTO.getPermanentSuspensionRequest().setStatusWiseCount(getCardResponse(
                    permanentSuspension, totalCount, statusCounts, hpProfileStatuses, "permanent_suspension_request",
                    TOTAL_PERMANENT_SUSPENSION_REQUESTS));
        }
        return responseTO;
    }

    private static List<StatusWiseCountTO> getCardResponse(List<StatusWiseCountTO> statusWiseCountResponseTos, BigInteger totalCount,
                                                           List<IStatusCount> statusCounts, List<BigInteger> hpProfileStatuses,
                                                           String applicationType, String totalKey) {
        for (BigInteger status : hpProfileStatuses) {
            for (IStatusCount sc : statusCounts) {
                if (status.equals(sc.getProfileStatus()) && sc.getApplicationTypeId() != null && applicationIds.get(applicationType).contains(sc.getApplicationTypeId())) {
                    System.out.println(status + " , " + sc.getProfileStatus() + " , " + sc.getApplicationTypeId() + " , " + applicationIds.get(applicationType));
                    Optional<StatusWiseCountTO> first = statusWiseCountResponseTos.stream()
                            .filter(r -> r.getId().equals(sc.getProfileStatus())).findFirst();
                    if (first.isPresent()) {
                        first.get().setCount(first.get().getCount().add(sc.getCount()));
                        totalCount = totalCount.add(sc.getCount());

                    }
                }
            }
        }
        StatusWiseCountTO count = new StatusWiseCountTO(totalKey, totalCount, BigInteger.ZERO);
        statusWiseCountResponseTos.add(count);
        return statusWiseCountResponseTos;
    }

    private List<StatusWiseCountTO> fetchStatusWiseCountBasedOnLoggedInUser(BigInteger applicationTypeId, User loggedInUser) throws InvalidRequestException {

        UserGroup group = loggedInUser.getGroup();
        BigInteger groupId = group.getId();

        /**
         * Group - College Dean
         */
        if (Group.COLLEGE.getDescription().equals(group.getName())) {
            BigInteger collegeId = iCollegeProfileDaoService.findByUserId(loggedInUser.getId()).getCollege().getId();
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
            } else if (ApplicationType.HP_TEMPORARY_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> temporarySuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForSmc(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), smcProfileId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, temporarySuspensionStatusWiseCount);
            } else if (ApplicationType.HP_PERMANENT_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> permanentSuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForSmc(ApplicationType.HP_PERMANENT_SUSPENSION.getId(), smcProfileId);
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, permanentSuspensionStatusWiseCount);
            } else if (ApplicationType.HP_ACTIVATE_LICENSE.getId().equals(applicationTypeId)) {
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
            } else if (ApplicationType.HP_TEMPORARY_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> temporarySuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForNmc(ApplicationType.HP_TEMPORARY_SUSPENSION.getId());
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, temporarySuspensionStatusWiseCount);
            } else if (ApplicationType.HP_PERMANENT_SUSPENSION.getId().equals(applicationTypeId)) {
                List<IStatusWiseCount> permanentSuspensionStatusWiseCount = iFetchCountOnCardRepository.fetchUserSpecificSuspensionAndActivateStatusWiseCountForNmc(ApplicationType.HP_PERMANENT_SUSPENSION.getId());
                return fetchCountConsideringCompositeApplicationTypes(primaryStatusWiseCount, permanentSuspensionStatusWiseCount);
            } else if (ApplicationType.HP_ACTIVATE_LICENSE.getId().equals(applicationTypeId)) {
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

    public List<StatusWiseCountTO> getDefault() {
        List<StatusWiseCountTO> response = new ArrayList<>();
        response.add(StatusWiseCountTO.builder().id(Action.SUBMIT.getId()).name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.APPROVED.getId()).name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.QUERY_RAISE.getId()).name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.REJECT.getId()).name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.PERMANENT_SUSPEND.getId()).name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.TEMPORARY_SUSPEND.getId()).name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build());
        return response;
    }
}
