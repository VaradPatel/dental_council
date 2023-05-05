package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardRequestTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
import in.gov.abdm.nmr.entity.CollegeProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetailsMapper;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsCustomRepository;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * A class that implements all the methods of the interface IFetchSpecificDetailsService
 * which deals with dashboard count, dashboard fetch specific details
 */
@Service
@Slf4j
public class FetchSpecificDetailsServiceImpl implements IFetchSpecificDetailsService {

    /**
     * Injecting IFetchSpecificDetailsRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchSpecificDetailsRepository iFetchSpecificDetailsRepository;

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

    /**
     * Injecting ISmcProfileRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private ISmcProfileRepository iSmcProfileRepository;

    @Autowired
    private ICollegeProfileDaoService collegeProfileDaoService;

    @Override
    public List<FetchSpecificDetailsResponseTO> fetchSpecificDetails(String groupName, String applicationType, String workFlowStatus) throws InvalidRequestException {
        validateGroupName(groupName);
        validateApplicationType(applicationType);
        validateWorkFlowStatus(workFlowStatus);

        return fetchDetailsForListingByStatus(groupName, applicationType, workFlowStatus)
                .stream()
                .map(fetchSpecificDetails -> {
                    FetchSpecificDetailsResponseTO fetchSpecificDetailsResponseTO = iFetchSpecificDetailsMapper.toFetchSpecificDetailsResponseTO(fetchSpecificDetails);

                    if (Group.COLLEGE.getDescription().equals(fetchSpecificDetails.getGroupName())) {
                        fetchSpecificDetailsResponseTO.setCollegeVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if (Group.SMC.getDescription().equals(fetchSpecificDetails.getGroupName())) {
                        fetchSpecificDetailsResponseTO.setCouncilVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if (Group.NMC.getDescription().equals(fetchSpecificDetails.getGroupName())) {
                        fetchSpecificDetailsResponseTO.setNMCVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if (fetchSpecificDetails.getDateOfSubmission() != null) {
                        long diffInMillis = Math.abs(new Date().getTime() - fetchSpecificDetails.getDateOfSubmission().getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                        fetchSpecificDetailsResponseTO.setPendency(BigInteger.valueOf(diff));
                    }
                    return fetchSpecificDetailsResponseTO;
                })
                .toList();
    }

    private List<IFetchSpecificDetails> fetchDetailsForListingByStatus(String groupName, String applicationType, String workFlowStatus) {
        if (WorkflowStatus.APPROVED.getDescription().equals(workFlowStatus)) {
            return iFetchSpecificDetailsRepository.fetchDetailsWithApprovedStatusForListing(groupName, applicationType, workFlowStatus);
        } else if (WorkflowStatus.PENDING.getDescription().equals(workFlowStatus)) {
            return iFetchSpecificDetailsRepository.fetchDetailsWithPendingStatusForListing(groupName, applicationType, workFlowStatus);
        }
        return iFetchSpecificDetailsRepository.fetchDetailsForListing(groupName, applicationType, workFlowStatus);
    }

    private void validateGroupName(String groupName) throws InvalidRequestException {
        if (groupName == null || Arrays.stream(Group.values()).noneMatch(t -> t.getDescription().equals(groupName))) {
            throw new InvalidRequestException(NMRError.INVALID_GROUP.getCode(),NMRError.INVALID_GROUP.getMessage());
        }
    }

    private void validateApplicationType(String applicationType) throws InvalidRequestException {
        if (applicationType == null || Arrays.stream(ApplicationType.values()).noneMatch(t -> t.getDescription().equals(applicationType))) {
            throw new InvalidRequestException(NMRError.INVALID_APPLICATION_TYPE.getCode(),NMRError.INVALID_APPLICATION_TYPE.getMessage());
        }
    }

    private void validateWorkFlowStatus(String workFlowStatus) throws InvalidRequestException {
        if (workFlowStatus == null || Arrays.stream(WorkflowStatus.values()).noneMatch(t -> t.getDescription().equals(workFlowStatus))) {
            throw new InvalidRequestException(NMRError.INVALID_WORK_FLOW_STATUS.getCode(),NMRError.INVALID_WORK_FLOW_STATUS.getMessage());
        }
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