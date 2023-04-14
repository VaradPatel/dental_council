package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchCountOnCardInnerResponseTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.mapper.IStatusCount;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.repository.IFetchCountOnCardRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.mapper.IStatusWiseCountMapper;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@Service
@Slf4j
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
                    "permanent_suspension_request", List.of(ApplicationType.HP_PERMANENT_SUSPENSION.getId()),
                    "consolidated_suspension_request", List.of(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), ApplicationType.HP_PERMANENT_SUSPENSION.getId()));


    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard() throws AccessDeniedException {
        User loggedInUser = accessControlService.getLoggedInUser();
        String groupName = loggedInUser.getGroup().getName();
        if (loggedInUser == null) {
            log.error("User :{} don't have permission to access on this server", loggedInUser.getUserName());
            throw new AccessDeniedException(ACCESS_FORBIDDEN);
        }
        FetchCountOnCardResponseTO responseTO = new FetchCountOnCardResponseTO();
        BigInteger totalCount = BigInteger.ZERO;
        List<BigInteger> hpProfileStatuses = Arrays.stream(HpProfileStatus.values()).map(s -> s.getId()).collect(Collectors.toList());

        if (Group.SMC.getDescription().equals(groupName)) {
            log.info("Processing Cards service for SMC: {} ", loggedInUser.getUserName());
            BigInteger smcProfileId = iSmcProfileRepository.getSmcIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForSmc(smcProfileId);
            log.debug("Fetched statusCounts detail successfully for SMC : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequests(responseTO, totalCount, hpProfileStatuses, statusCounts);
            populateHealthProfessionalSuspensionRequests(responseTO, totalCount, hpProfileStatuses, statusCounts);
        }

        if (Group.COLLEGE.getDescription().equals(groupName)) {
            log.info("Processing Cards service for College: {} ", loggedInUser.getUserName());
            BigInteger collegeId = iCollegeProfileDaoService.findByUserId(loggedInUser.getId()).getCollege().getId();
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForCollege(collegeId);
            log.debug("Fetched statusCounts detail successfully for college : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequests(responseTO, totalCount, hpProfileStatuses, statusCounts);
        }

        if (Group.NMC.getDescription().equals(groupName)) {
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForNmc();
            log.debug("Fetched statusCounts detail successfully for NMC : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequests(responseTO, totalCount, hpProfileStatuses, statusCounts);
            populateHealthProfessionalSuspensionRequests(responseTO, totalCount, hpProfileStatuses, statusCounts);
        }

        if (Group.NBE.getDescription().equals(groupName)) {
            log.info("Processing Cards service for NBE: {} ", loggedInUser.getUserName());
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForNbe();
            log.debug("Fetched statusCounts detail successfully for NBE : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequests(responseTO, totalCount, hpProfileStatuses, statusCounts);

        }
        return responseTO;
    }

    private void populateHealthProfessionalSuspensionRequests(FetchCountOnCardResponseTO responseTO, BigInteger totalCount, List<BigInteger> hpProfileStatuses, List<IStatusCount> statusCounts) {
        final List<StatusWiseCountTO> temporarySuspension = getDefaultCards(TOTAL_TEMPORARY_SUSPENSION_REQUESTS);
        responseTO.setTemporarySuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get("temporary_suspension_request"), ",")).build());
        responseTO.getTemporarySuspensionRequest().setStatusWiseCount(getCardResponse(
                temporarySuspension, totalCount, statusCounts, hpProfileStatuses, "temporary_suspension_request",
                TOTAL_TEMPORARY_SUSPENSION_REQUESTS));

        final List<StatusWiseCountTO> permanentSuspension = getDefaultCards(TOTAL_PERMANENT_SUSPENSION_REQUESTS);
        responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get("permanent_suspension_request"), ",")).build());
        responseTO.getPermanentSuspensionRequest().setStatusWiseCount(getCardResponse(
                permanentSuspension, totalCount, statusCounts, hpProfileStatuses, "permanent_suspension_request",
                TOTAL_PERMANENT_SUSPENSION_REQUESTS));

        List<StatusWiseCountTO> consolidatedSuspensions  = new ArrayList<>();
        consolidatedSuspensions.add(StatusWiseCountTO.builder().name(TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS).count(BigInteger.ZERO).build());
        temporarySuspension.forEach(ts -> {
            if(PENDING.equals(ts.getName())){
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_PENDING_TEMPORARY_SUSPENSION_REQUESTS).count(ts.getCount()).build());
            }else if (APPROVED.equals(ts.getName())){
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_APPROVED_TEMPORARY_SUSPENSION_REQUESTS).count(ts.getCount()).build());
            }else if(TOTAL_TEMPORARY_SUSPENSION_REQUESTS.equals(ts.getName())){
               consolidatedSuspensions.get(0).getCount().add(ts.getCount());
            }
        });

        permanentSuspension.forEach(ps -> {
            if(PENDING.equals(ps.getName())){
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_PENDING_PERMANENT_SUSPENSION_REQUESTS).count(ps.getCount()).build());
            }else if (APPROVED.equals(ps.getName())){
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_APPROVED_PERMANENT_SUSPENSION_REQUESTS).count(ps.getCount()).build());
            }else if(TOTAL_TEMPORARY_SUSPENSION_REQUESTS.equals(ps.getName())){
                consolidatedSuspensions.get(0).getCount().add(ps.getCount());
            }
        });

        responseTO.setConsolidatedSuspensionRequest(FetchCountOnCardInnerResponseTO.builder().
                applicationTypeIds(StringUtils.join(applicationIds.get("consolidated_suspension_request"),",")).statusWiseCount(consolidatedSuspensions).build());


    }

    private void populateHealthProfessionalRegistrationAndModificationRequests(FetchCountOnCardResponseTO responseTO, BigInteger totalCount, List<BigInteger> hpProfileStatuses, List<IStatusCount> statusCounts) {
        final List<StatusWiseCountTO> hpRegistration = getDefaultCards(TOTAL_HP_REGISTRATION_REQUESTS);
        responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get("hp_registration_request"), ",")).build());
        responseTO.getHpRegistrationRequest().setStatusWiseCount(getCardResponse(
                hpRegistration, totalCount, statusCounts, hpProfileStatuses, "hp_registration_request",
                TOTAL_HP_REGISTRATION_REQUESTS));

        final List<StatusWiseCountTO> hpModification = getDefaultCards(TOTAL_HP_MODIFICATION_REQUESTS);
        responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get("hp_modification_request"), ",")).build());
        responseTO.getHpModificationRequest().setStatusWiseCount(getCardResponse(
                hpModification, totalCount, statusCounts, hpProfileStatuses, "hp_modification_request",
                TOTAL_HP_MODIFICATION_REQUESTS));
    }

    private static List<StatusWiseCountTO> getCardResponse(List<StatusWiseCountTO> statusWiseCountResponseTos, BigInteger totalCount,
                                                           List<IStatusCount> statusCounts, List<BigInteger> hpProfileStatuses,
                                                           String applicationType, String totalKey) {
        for (BigInteger status : hpProfileStatuses) {
            for (IStatusCount sc : statusCounts) {
                if (status.equals(sc.getProfileStatus()) && sc.getApplicationTypeId() != null && applicationIds.get(applicationType).contains(sc.getApplicationTypeId())) {
                    Optional<StatusWiseCountTO> first = statusWiseCountResponseTos.stream()
                            .filter(r -> r.getId() != null && r.getId().equals(sc.getProfileStatus())).findFirst();
                    if (first.isPresent()) {
                        first.get().setCount(first.get().getCount().add(sc.getCount()));
                        totalCount = totalCount.add(sc.getCount());

                    }
                }
            }
        }
        statusWiseCountResponseTos.get(0).setCount(totalCount);
        return statusWiseCountResponseTos;
    }

    public List<StatusWiseCountTO> getDefaultCards(String totalCardLabel) {
        List<StatusWiseCountTO> response = new ArrayList<>();
        response.add(StatusWiseCountTO.builder().name(totalCardLabel).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.SUBMIT.getId()).name(WorkflowStatus.PENDING.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.APPROVED.getId()).name(WorkflowStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.QUERY_RAISE.getId()).name(WorkflowStatus.QUERY_RAISED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.REJECT.getId()).name(WorkflowStatus.REJECTED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.PERMANENT_SUSPEND.getId()).name(WorkflowStatus.SUSPENDED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(Action.TEMPORARY_SUSPEND.getId()).name(WorkflowStatus.BLACKLISTED.getDescription()).count(BigInteger.ZERO).build());
        log.debug("Fetched default Card Count detail successfully");
        return response;
    }
}
