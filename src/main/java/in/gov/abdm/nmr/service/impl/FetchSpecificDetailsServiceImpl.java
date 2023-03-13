package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardRequestTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetailsMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import in.gov.abdm.nmr.service.IUserDaoService;
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

    /**
     * Injecting ICollegeDeanRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private ICollegeDeanRepository iCollegeDeanRepository;

    /**
     * Injecting ICollegeRegistrarRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private ICollegeRegistrarRepository iCollegeRegistrarRepository;

    /**
     * Injecting ICollegeRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private ICollegeRepository iCollegeRepository;

    @Override
    public List<FetchSpecificDetailsResponseTO> fetchSpecificDetails(String groupName, String applicationType, String workFlowStatus) throws InvalidRequestException {
        validateGroupName(groupName);
        validateApplicationType(applicationType);
        validateWorkFlowStatus(workFlowStatus);

        return fetchDetailsForListingByStatus(groupName, applicationType, workFlowStatus)
                .stream()
                .map(fetchSpecificDetails -> {
                    FetchSpecificDetailsResponseTO fetchSpecificDetailsResponseTO = iFetchSpecificDetailsMapper.toFetchSpecificDetailsResponseTO(fetchSpecificDetails);

                    if (Group.COLLEGE_ADMIN.getDescription().equals(fetchSpecificDetails.getGroupName()) ||
                            Group.COLLEGE_DEAN.getDescription().equals(fetchSpecificDetails.getGroupName()) ||
                            Group.COLLEGE_REGISTRAR.getDescription().equals(fetchSpecificDetails.getGroupName())) {
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
            throw new InvalidRequestException(INVALID_GROUP);
        }
    }

    private void validateApplicationType(String applicationType) throws InvalidRequestException {
        if (applicationType == null || Arrays.stream(ApplicationType.values()).noneMatch(t -> t.getDescription().equals(applicationType))) {
            throw new InvalidRequestException(INVALID_APPLICATION_TYPE);
        }
    }

    private void validateWorkFlowStatus(String workFlowStatus) throws InvalidRequestException {
        if (workFlowStatus == null || Arrays.stream(WorkflowStatus.values()).noneMatch(t -> t.getDescription().equals(workFlowStatus))) {
            throw new InvalidRequestException(INVALID_WORK_FLOW_STATUS);
        }
    }

    /**
     * Fetches card details for a given set of parameters.
     *
     * @param workFlowStatusId  the workflow status ID
     * @param applicationTypeId the application type ID
     * @param userGroupStatus   the user group status
     * @param pageNo            the page number
     * @param size              the size of the page
     * @param sortBy            the sort parameter
     * @param sortOrder         the sort order
     * @return the DashboardResponseTO containing the card details
     * @throws InvalidRequestException if the request is invalid
     */
    @Override
    public DashboardResponseTO fetchCardDetails(String workFlowStatusId, String applicationTypeId, String userGroupStatus,
                                                String search, String value, int pageNo, int size,
                                                String sortBy, String sortOrder) throws InvalidRequestException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetail = userDaoService.findByUsername(userName);
        BigInteger groupId = userDetail.getGroup().getId();
        BigInteger userId = userDetail.getId();
        DashboardRequestParamsTO dashboardRequestParamsTO = new DashboardRequestParamsTO();
        dashboardRequestParamsTO.setWorkFlowStatusId(workFlowStatusId);
        dashboardRequestParamsTO.setApplicationTypeId(applicationTypeId);
        if(StringUtils.isNotBlank(search)){
            if(value !=null && !value.isBlank()){
                switch (search.toLowerCase()){
                    case APPLICANT_FULL_NAME_IN_LOWER_CASE: dashboardRequestParamsTO.setApplicantFullName(value);
                        break;
                    case COUNCIL_NAME_IN_LOWER_CASE: dashboardRequestParamsTO.setCouncilName(value);
                        break;
                    case REGISTRATION_NUMBER_IN_LOWER_CASE: dashboardRequestParamsTO.setRegistrationNumber(value);
                        break;
                    case SMC_ID_IN_LOWER_CASE: {
                        if (groupId.equals(Group.COLLEGE_DEAN.getId()) || groupId.equals(Group.COLLEGE_REGISTRAR.getId()) || groupId.equals(Group.COLLEGE_ADMIN.getId())
                                || groupId.equals(Group.NMC.getId()) || groupId.equals(Group.NBE.getId())) {
                            dashboardRequestParamsTO.setSmcId(value);
                        }
                    }
                    break;
                    case SEARCH_IN_LOWER_CASE: dashboardRequestParamsTO.setSearch(value);
                        break;
                    default: throw new InvalidRequestException(INVALID_SEARCH_CRITERIA_FOR_GET_CARD_DETAIL);
                }
            }
            else{
                throw new InvalidRequestException(MISSING_SEARCH_VALUE);
            }
        }

        String column = mapColumnToTable(sortBy);
        dashboardRequestParamsTO.setSortBy(column);
        dashboardRequestParamsTO.setUserGroupId(groupId);
        dashboardRequestParamsTO.setUserGroupStatus(userGroupStatus);
        if (groupId.equals(Group.SMC.getId())) {
            dashboardRequestParamsTO.setCouncilId(iSmcProfileRepository.findByUserId(userId).getStateMedicalCouncil().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_DEAN.getId())) {
            dashboardRequestParamsTO.setCollegeId(iCollegeDeanRepository.findByUserId(userId).getCollege().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())) {
            dashboardRequestParamsTO.setCollegeId(iCollegeRegistrarRepository.findByUserId(userId).getCollege().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_ADMIN.getId())) {
            dashboardRequestParamsTO.setCollegeId(iCollegeRepository.findByUserId(userId).getId().toString());
        }

        final String sortingOrder = (sortOrder == null || sortOrder.trim().isEmpty()) ? DEFAULT_SORT_ORDER : sortOrder;
        dashboardRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = Math.min(MAX_DATA_SIZE, size);
        Pageable pageable = PageRequest.of(pageNo, dataLimit);
        return iFetchSpecificDetailsCustomRepository.fetchDashboardData(dashboardRequestParamsTO, pageable);
    }

    /**
     * This method fetches the dashboard data based on the input request.
     *
     * @param dashboardRequestTO The request object containing the parameters for fetching dashboard data.
     * @return DashboardResponseTO The response object containing the details fetched from dashboard.
     * @throws InvalidRequestException If the input request is invalid.
     */
    @Override
    public DashboardResponseTO fetchDashboardData(DashboardRequestTO dashboardRequestTO) throws InvalidRequestException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetail = userDaoService.findByUsername(userName);
        String column = mapColumnToTable(dashboardRequestTO.getSortBy());
        BigInteger groupId = userDetail.getGroup().getId();
        BigInteger userId = userDetail.getId();
        String sortOrder = dashboardRequestTO.getSortOrder();
        DashboardRequestParamsTO dashboardRequestParamsTO = new DashboardRequestParamsTO();
        dashboardRequestParamsTO.setWorkFlowStatusId(dashboardRequestTO.getWorkFlowStatusId());
        dashboardRequestParamsTO.setApplicationTypeId(dashboardRequestTO.getApplicationTypeId());
        if(dashboardRequestTO.getSearch() !=null){
            if(dashboardRequestTO.getValue() !=null && !dashboardRequestTO.getValue().isBlank()){
                switch (dashboardRequestTO.getSearch().toLowerCase()){
                    case COLLEGE_ID_IN_LOWER_CASE: dashboardRequestParamsTO.setCollegeId(dashboardRequestTO.getValue());
                        break;
                    case NAME_IN_LOWER_CASE: dashboardRequestParamsTO.setCouncilName(dashboardRequestTO.getValue());
                        break;
                    case REGISTRATION_NUMBER_IN_LOWER_CASE: dashboardRequestParamsTO.setRegistrationNumber(dashboardRequestTO.getValue());
                        break;
                    case SMC_ID_IN_LOWER_CASE: {
                        if (groupId.equals(Group.COLLEGE_DEAN.getId()) || groupId.equals(Group.COLLEGE_REGISTRAR.getId()) || groupId.equals(Group.COLLEGE_ADMIN.getId())
                                || groupId.equals(Group.NMC.getId()) || groupId.equals(Group.NBE.getId())) {
                            dashboardRequestParamsTO.setSmcId(dashboardRequestTO.getValue());
                        }
                    }
                    break;
                    case WORK_FLOW_STATUS_IN_LOWER_CASE: dashboardRequestParamsTO.setWorkFlowStatusId(dashboardRequestTO.getValue());
                        break;
                    case SEARCH_IN_LOWER_CASE: dashboardRequestParamsTO.setSearch(dashboardRequestTO.getValue());
                        break;
                    default: throw new InvalidRequestException(INVALID_SEARCH_CRITERIA_FOR_POST_CARD_DETAIL);
                }
            }
            else{
                throw new InvalidRequestException(MISSING_SEARCH_VALUE);
            }
        }

        dashboardRequestParamsTO.setSortBy(column);
        dashboardRequestParamsTO.setUserGroupId(groupId);
        dashboardRequestParamsTO.setUserGroupStatus(dashboardRequestTO.getUserGroupStatus());
        if (groupId.equals(Group.SMC.getId())) {
            SMCProfile smcProfile = iSmcProfileRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCouncilId(smcProfile.getStateMedicalCouncil().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_DEAN.getId())) {
            CollegeDean collegeDean = iCollegeDeanRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCollegeId(collegeDean.getCollege().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())) {
            CollegeRegistrar collegeRegistrar = iCollegeRegistrarRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCollegeId(collegeRegistrar.getCollege().getId().toString());
        }
        final String sortingOrder = (sortOrder == null || sortOrder.trim().isEmpty()) ? DEFAULT_SORT_ORDER : sortOrder;
        dashboardRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = Math.min(MAX_DATA_SIZE, dashboardRequestTO.getSize());
        Pageable pageable = PageRequest.of(dashboardRequestTO.getPageNo(), dataLimit);
        return iFetchSpecificDetailsCustomRepository.fetchDashboardData(dashboardRequestParamsTO, pageable);
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
        return columnToSortMap.getOrDefault(columnToSort, " rd.created_at ");
    }
}