package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetailsMapper;
import in.gov.abdm.nmr.mapper.IStatusCount;
import in.gov.abdm.nmr.mapper.IStatusWiseCountMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;
import in.gov.abdm.nmr.service.IDashboardService;
import in.gov.abdm.nmr.service.IUserDaoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.*;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@Service
@Slf4j
public class DashboardServiceImpl implements IDashboardService {

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

    /**
     * Mapper Interface to transform the IFetchSpecificDetails Bean
     * to the FetchSpecificDetailsResponseTO Bean transferring its contents
     */
    @Autowired
    private IFetchSpecificDetailsMapper iFetchSpecificDetailsMapper;

    /**
     * Injecting IFetchSpecificDetailsCustomRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchSpecificDetailsCustomRepository iFetchSpecificDetailsCustomRepository;

    /**
     * Injecting IUserRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IUserDaoService userDaoService;
    @Autowired
    private ICollegeProfileDaoService collegeProfileDaoService;


    private static final String HP_REGISTRATION_REQUEST = "hp_registration_request";
    private static final String HP_MODIFICATION_REQUEST = "hp_modification_request";
    private static final String TEMPORARY_SUSPENSION_REQUEST = "temporary_suspension_request";
    private static final String PERMANENT_SUSPENSION_REQUEST = "permanent_suspension_request";
    private static final String CONSOLIDATED_SUSPENSION_REQUEST = "consolidated_suspension_request";

    private static final Map<String, List<BigInteger>> applicationIds =
            Map.of(HP_REGISTRATION_REQUEST, List.of(ApplicationType.HP_REGISTRATION.getId(), ApplicationType.FOREIGN_HP_REGISTRATION.getId()),
                    HP_MODIFICATION_REQUEST, List.of(ApplicationType.HP_MODIFICATION.getId(), ApplicationType.QUALIFICATION_ADDITION.getId()),
                    TEMPORARY_SUSPENSION_REQUEST, List.of(ApplicationType.HP_TEMPORARY_SUSPENSION.getId()),
                    PERMANENT_SUSPENSION_REQUEST, List.of(ApplicationType.HP_PERMANENT_SUSPENSION.getId()),
                    CONSOLIDATED_SUSPENSION_REQUEST, List.of(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), ApplicationType.HP_PERMANENT_SUSPENSION.getId()));


    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard() throws AccessDeniedException {
        User loggedInUser = accessControlService.getLoggedInUser();
        if (loggedInUser == null) {
            log.error("User don't have permission to access on dashboard card details");
            throw new AccessDeniedException(NMRError.ACCESS_FORBIDDEN.getMessage());
        }
        String groupName = loggedInUser.getGroup().getName();
        FetchCountOnCardResponseTO responseTO = new FetchCountOnCardResponseTO();
        BigInteger totalCount = BigInteger.ZERO;
        List<BigInteger> dashboardStauses = Arrays.stream(DashboardStatus.values()).map(DashboardStatus::getId).toList();

        if (Group.SMC.getDescription().equals(groupName)) {
            log.info("Processing Cards service for SMC: {} ", loggedInUser.getUserName());
            BigInteger smcProfileId = iSmcProfileRepository.getSmcIdByUserId(loggedInUser.getId()).get(0);
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForSmc(smcProfileId);
            log.debug("Fetched statusCounts detail successfully for SMC : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequestsForSmc(responseTO, totalCount, dashboardStauses, statusCounts);
            populateHealthProfessionalSuspensionRequests(responseTO, totalCount, dashboardStauses, statusCounts);
        }

        if (Group.COLLEGE.getDescription().equals(groupName)) {
            log.info("Processing Cards service for College: {} ", loggedInUser.getUserName());
            BigInteger collegeId = iCollegeProfileDaoService.findByUserId(loggedInUser.getId()).getCollege().getId();
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForCollege(collegeId);
            log.debug("Fetched statusCounts detail successfully for college : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequests(responseTO, totalCount, dashboardStauses, statusCounts);
        }

        if (Group.NMC.getDescription().equals(groupName)) {
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForNmc();
            log.debug("Fetched statusCounts detail successfully for NMC : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequests(responseTO, totalCount, dashboardStauses, statusCounts);
            populateHealthProfessionalSuspensionRequests(responseTO, totalCount, dashboardStauses, statusCounts);
        }

        if (Group.NBE.getDescription().equals(groupName)) {
            log.info("Processing Cards service for NBE: {} ", loggedInUser.getUserName());
            List<IStatusCount> statusCounts = iFetchCountOnCardRepository.fetchCountForNbe();
            log.debug("Fetched statusCounts detail successfully for NBE : {}", loggedInUser.getUserName());
            populateHealthProfessionalRegistrationAndModificationRequests(responseTO, totalCount, dashboardStauses, statusCounts);

        }
        return responseTO;
    }

    private void populateHealthProfessionalSuspensionRequests(FetchCountOnCardResponseTO responseTO, BigInteger totalCount, List<BigInteger> dashboardStauses, List<IStatusCount> statusCounts) {
        final List<StatusWiseCountTO> temporarySuspension = getDefaultCards(TOTAL_TEMPORARY_SUSPENSION_REQUESTS);
        responseTO.setTemporarySuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get(TEMPORARY_SUSPENSION_REQUEST), ",")).build());
        responseTO.getTemporarySuspensionRequest().setStatusWiseCount(getCardResponse(
                temporarySuspension, totalCount, statusCounts, dashboardStauses, TEMPORARY_SUSPENSION_REQUEST));

        final List<StatusWiseCountTO> permanentSuspension = getDefaultCards(TOTAL_PERMANENT_SUSPENSION_REQUESTS);
        responseTO.setPermanentSuspensionRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get(PERMANENT_SUSPENSION_REQUEST), ",")).build());
        responseTO.getPermanentSuspensionRequest().setStatusWiseCount(getCardResponse(
                permanentSuspension, totalCount, statusCounts, dashboardStauses, PERMANENT_SUSPENSION_REQUEST));

        List<StatusWiseCountTO> consolidatedSuspensions = new ArrayList<>();
        consolidatedSuspensions.add(StatusWiseCountTO.builder().name(TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS).count(BigInteger.ZERO).build());
        temporarySuspension.forEach(ts -> {
            if (PENDING.equals(ts.getName())) {
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_PENDING_TEMPORARY_SUSPENSION_REQUESTS).count(ts.getCount()).build());
            } else if (APPROVED.equals(ts.getName())) {
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_APPROVED_TEMPORARY_SUSPENSION_REQUESTS).count(ts.getCount()).build());
            } else if (TOTAL_TEMPORARY_SUSPENSION_REQUESTS.equals(ts.getName())) {
                consolidatedSuspensions.get(0).getCount().add(ts.getCount());
            }
        });

        permanentSuspension.forEach(ps -> {
            if (PENDING.equals(ps.getName())) {
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_PENDING_PERMANENT_SUSPENSION_REQUESTS).count(ps.getCount()).build());
            } else if (APPROVED.equals(ps.getName())) {
                consolidatedSuspensions.add(StatusWiseCountTO.builder().name(CONSOLIDATED_APPROVED_PERMANENT_SUSPENSION_REQUESTS).count(ps.getCount()).build());
            } else if (TOTAL_TEMPORARY_SUSPENSION_REQUESTS.equals(ps.getName())) {
                consolidatedSuspensions.get(0).getCount().add(ps.getCount());
            }
        });

        responseTO.setConsolidatedSuspensionRequest(FetchCountOnCardInnerResponseTO.builder().
                applicationTypeIds(StringUtils.join(applicationIds.get(CONSOLIDATED_SUSPENSION_REQUEST), ",")).statusWiseCount(consolidatedSuspensions).build());


    }

    private void populateHealthProfessionalRegistrationAndModificationRequestsForSmc(FetchCountOnCardResponseTO responseTO, BigInteger totalCount, List<BigInteger> dashboardStatuses, List<IStatusCount> statusCounts) {
        final List<StatusWiseCountTO> hpRegistration = getDefaultCardsForSMC(TOTAL_HP_REGISTRATION_REQUESTS);
        responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get(HP_REGISTRATION_REQUEST), ",")).build());
        responseTO.getHpRegistrationRequest().setStatusWiseCount(getCardResponse(
                hpRegistration, totalCount, statusCounts, dashboardStatuses, HP_REGISTRATION_REQUEST));

        final List<StatusWiseCountTO> hpModification = getDefaultCardsForSMC(TOTAL_HP_MODIFICATION_REQUESTS);
        responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get(HP_MODIFICATION_REQUEST), ",")).build());
        responseTO.getHpModificationRequest().setStatusWiseCount(getCardResponse(
                hpModification, totalCount, statusCounts, dashboardStatuses, HP_MODIFICATION_REQUEST));
    }

    private void populateHealthProfessionalRegistrationAndModificationRequests(FetchCountOnCardResponseTO responseTO, BigInteger totalCount, List<BigInteger> dashboardStatuses, List<IStatusCount> statusCounts) {
        final List<StatusWiseCountTO> hpRegistration = getDefaultCards(TOTAL_HP_REGISTRATION_REQUESTS);
        responseTO.setHpRegistrationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get(HP_REGISTRATION_REQUEST), ",")).build());
        responseTO.getHpRegistrationRequest().setStatusWiseCount(getCardResponse(
                hpRegistration, totalCount, statusCounts, dashboardStatuses, HP_REGISTRATION_REQUEST));

        final List<StatusWiseCountTO> hpModification = getDefaultCards(TOTAL_HP_MODIFICATION_REQUESTS);
        responseTO.setHpModificationRequest(FetchCountOnCardInnerResponseTO.builder()
                .applicationTypeIds(StringUtils.join(applicationIds.get(HP_MODIFICATION_REQUEST), ",")).build());
        responseTO.getHpModificationRequest().setStatusWiseCount(getCardResponse(
                hpModification, totalCount, statusCounts, dashboardStatuses, HP_MODIFICATION_REQUEST));
    }

    private static List<StatusWiseCountTO> getCardResponse(List<StatusWiseCountTO> statusWiseCountResponseTos, BigInteger totalCount,
                                                           List<IStatusCount> statusCounts, List<BigInteger> dashboardStatuses,
                                                           String applicationType) {
        for (BigInteger status : dashboardStatuses) {
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

    public static List<StatusWiseCountTO> getDefaultCards(String totalCardLabel) {
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

    public List<StatusWiseCountTO> getDefaultCardsForSMC(String totalCardLabel) {
        List<StatusWiseCountTO> response = new ArrayList<>();
        response.add(StatusWiseCountTO.builder().name(totalCardLabel).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(DashboardStatus.PENDING.getId()).name(DashboardStatus.PENDING.getDescription()).count(BigInteger.ZERO).build());
        // this has to un comment when we need add college_verified.
        response.add(StatusWiseCountTO.builder().id(DashboardStatus.COLLEGE_VERIFIED.getId()).name(DashboardStatus.COLLEGE_VERIFIED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(DashboardStatus.APPROVED.getId()).name(DashboardStatus.APPROVED.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(DashboardStatus.QUERY_RAISE.getId()).name(DashboardStatus.QUERY_RAISE.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(DashboardStatus.REJECT.getId()).name(DashboardStatus.REJECT.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(DashboardStatus.PERMANENT_SUSPEND.getId()).name(DashboardStatus.PERMANENT_SUSPEND.getDescription()).count(BigInteger.ZERO).build());
        response.add(StatusWiseCountTO.builder().id(DashboardStatus.TEMPORARY_SUSPEND.getId()).name(DashboardStatus.TEMPORARY_SUSPEND.getDescription()).count(BigInteger.ZERO).build());
        log.debug("Fetched default Card Count detail successfully");
        return response;
    }


    /**
     * Maps the database column name to be used for sorting based on the columnToSort name.
     *
     * @param columnToSort - name of the column to be sorted
     * @return database column name to be used for sorting
     */
    private String mapColumnToTable(String columnToSort) {
        Map<String, String> columnToSortMap = new HashMap<>();
        columnToSortMap.put("doctorStatus", " doctor_status");
        columnToSortMap.put("smcStatus", " smc_status");
        columnToSortMap.put("collegeDeanStatus", " college_dean_status");
        columnToSortMap.put("collegeRegistrarStatus", " college_registrar_status");
        columnToSortMap.put("nmcStatus", " nmc_status");
        columnToSortMap.put("nbeStatus", " nbe_status");
        columnToSortMap.put("hpProfileId", " calculate.hp_profile_id");
        columnToSortMap.put("requestId", " calculate.request_id");
        columnToSortMap.put("registrationNo", " rd.registration_no");
        columnToSortMap.put("createdAt", " rd.created_at");
        columnToSortMap.put("councilName", " stmc.name");
        columnToSortMap.put("applicantFullName", " hp.full_name");
        columnToSortMap.put("pendency", " pendency");
        return columnToSortMap.getOrDefault(columnToSort, " pendency ");
    }

    private void setApplicationTypeIdAndUserGroupStatus(String applicationTypeId, String userGroupStatus,
                                                        DashboardRequestParamsTO dashboardRequestParamsTO) {
        if (TEMPORARY_AND_PERMANENT_SUSPENSION_APPLICATION_TYPE_ID.equals(applicationTypeId)) {
            if (CONSOLIDATED_PENDING_TEMPORARY_SUSPENSION_REQUESTS.equals(userGroupStatus)) {
                dashboardRequestParamsTO.setApplicationTypeId(TEMPORARY_SUSPENSION_APPLICATION_TYPE_ID);
                dashboardRequestParamsTO.setUserGroupStatus(Action.SUBMIT.getId().toString());
            } else if (CONSOLIDATED_APPROVED_TEMPORARY_SUSPENSION_REQUESTS.equals(userGroupStatus)) {
                dashboardRequestParamsTO.setApplicationTypeId(TEMPORARY_SUSPENSION_APPLICATION_TYPE_ID);
                dashboardRequestParamsTO.setUserGroupStatus(Action.APPROVED.getId().toString());
            } else if (CONSOLIDATED_PENDING_PERMANENT_SUSPENSION_REQUESTS.equals(userGroupStatus)) {
                dashboardRequestParamsTO.setApplicationTypeId(PERMANENT_SUSPENSION_APPLICATION_TYPE_ID);
                dashboardRequestParamsTO.setUserGroupStatus(Action.SUBMIT.getId().toString());
            } else if (CONSOLIDATED_APPROVED_PERMANENT_SUSPENSION_REQUESTS.equals(userGroupStatus)) {
                dashboardRequestParamsTO.setApplicationTypeId(PERMANENT_SUSPENSION_APPLICATION_TYPE_ID);
                dashboardRequestParamsTO.setUserGroupStatus(Action.APPROVED.getId().toString());
            } else if (TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS.equals(userGroupStatus)) {
                dashboardRequestParamsTO.setUserGroupStatus(TOTAL);
                dashboardRequestParamsTO.setApplicationTypeId(applicationTypeId);
            }
        } else {
            dashboardRequestParamsTO.setApplicationTypeId(applicationTypeId);
            dashboardRequestParamsTO.setUserGroupStatus(!userGroupStatus.contains(TOTAL) ? DashboardStatus.getDashboardStatus(userGroupStatus).getId().toString() : TOTAL);
        }
    }

    private void applyFiltersForCardDetails(String search, String value, BigInteger groupId, DashboardRequestParamsTO dashboardRequestParamsTO) throws InvalidRequestException {
        if (StringUtils.isNotBlank(search)) {
            if (value != null && !value.isBlank()) {
                switch (search.toLowerCase()) {
                    case APPLICANT_FULL_NAME_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setApplicantFullName(value);
                        break;
                    case COUNCIL_NAME_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setCouncilName(value);
                        break;
                    case REGISTRATION_NUMBER_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setRegistrationNumber(value);
                        break;
                    case GENDER_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setGender(value);
                        break;
                    case EMAIL_ID_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setEmailId(value);
                        break;
                    case MOBILE_NUMBER_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setMobileNumber(value);
                        break;
                    case YEAR_OF_REGISTRATION_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setYearOfRegistration(value);
                        break;
                    case SMC_ID_IN_LOWER_CASE: {
                        if (groupId.equals(Group.COLLEGE.getId())
                                || groupId.equals(Group.NMC.getId()) || groupId.equals(Group.NBE.getId())) {
                            dashboardRequestParamsTO.setSmcId(value);
                        }
                    }
                    break;
                    case SEARCH_IN_LOWER_CASE:
                        dashboardRequestParamsTO.setSearch(value);
                        break;
                    default:
                        throw new InvalidRequestException(NMRError.INVALID_SEARCH_CRITERIA_FOR_GET_CARD_DETAIL.getCode(), NMRError.INVALID_SEARCH_CRITERIA_FOR_GET_CARD_DETAIL.getMessage());
                }
            } else {
                throw new InvalidRequestException(NMRError.MISSING_SEARCH_VALUE.getCode(), NMRError.MISSING_SEARCH_VALUE.getMessage());
            }
        }
    }

    /**
     * Fetches card details for a given set of parameters.
     *
     * @param workFlowStatusId  the workflow status ID
     * @param applicationTypeId the application type ID
     * @param userGroupStatus   the user group status
     * @param pageNo            the page number
     * @param offset            the size of the page
     * @param sortBy            the sort parameter
     * @param sortOrder         the sort order
     * @return the DashboardResponseTO containing the card details
     * @throws InvalidRequestException if the request is invalid
     */
    @Override
    public DashboardResponseTO fetchCardDetails(String workFlowStatusId, String applicationTypeId, String userGroupStatus,
                                                String search, String value, int pageNo, int offset,
                                                String sortBy, String sortOrder) throws InvalidRequestException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Processing card-detail service for : {} ", userName);
        User userDetail = userDaoService.findByUsername(userName);
        BigInteger groupId = userDetail.getGroup().getId();
        BigInteger userId = userDetail.getId();
        DashboardRequestParamsTO dashboardRequestParamsTO = new DashboardRequestParamsTO();
        setApplicationTypeIdAndUserGroupStatus(applicationTypeId, userGroupStatus, dashboardRequestParamsTO);
        dashboardRequestParamsTO.setWorkFlowStatusId(workFlowStatusId);
        applyFiltersForCardDetails(search, value, groupId, dashboardRequestParamsTO);
        String column = mapColumnToTable(sortBy);
        dashboardRequestParamsTO.setSortBy(column);
        dashboardRequestParamsTO.setUserGroupId(groupId);
        if (groupId.equals(Group.SMC.getId())) {
            dashboardRequestParamsTO.setCouncilId(iSmcProfileRepository.findByUserId(userId).getStateMedicalCouncil().getId().toString());
        } else if (groupId.equals(Group.COLLEGE.getId())) {
            dashboardRequestParamsTO.setCollegeId(collegeProfileDaoService.findByUserId(userId).getCollege().getId().toString());
        }
        final String sortingOrder = (sortOrder == null || sortOrder.trim().isEmpty()) ? DEFAULT_SORT_ORDER : sortOrder;
        dashboardRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = Math.min(MAX_DATA_SIZE, offset);
        Pageable pageable = PageRequest.of(pageNo, dataLimit);
        return iFetchSpecificDetailsCustomRepository.fetchDashboardData(dashboardRequestParamsTO, pageable);
    }
}