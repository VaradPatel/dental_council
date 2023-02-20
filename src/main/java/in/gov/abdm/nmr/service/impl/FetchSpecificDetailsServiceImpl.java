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
    private IUserRepository userDetailRepository;

    /**
     * Injecting ISmcProfileRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private ISmcProfileRepository smcProfileRepository;

    /**
     * Injecting ICollegeDeanRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private ICollegeDeanRepository collegeDeanRepository;

    /**
     * Injecting ICollegeRegistrarRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private ICollegeRegistrarRepository collegeRegistrarRepository;

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

    @Override
    public DashboardResponseTO fetchDashboardData1(String workFlowStatusId, String applicationTypeId, String userGroupStatus,
                                                   String smcId, String name, String nmrId, String search, int pageNo, int size,
                                                   String sortBy, String sortOrder) throws InvalidRequestException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetail = userDetailRepository.findByUsername(userName);
        BigInteger groupId = userDetail.getGroup().getId();
        BigInteger userId = userDetail.getId();
        DashboardRequestParamsTO dashboardRequestParamsTO = new DashboardRequestParamsTO();
        dashboardRequestParamsTO.setWorkFlowStatusId(workFlowStatusId);
        dashboardRequestParamsTO.setApplicationTypeId(applicationTypeId);
        dashboardRequestParamsTO.setName(name);
        dashboardRequestParamsTO.setNmrId(nmrId);
        dashboardRequestParamsTO.setSearch(search);
        dashboardRequestParamsTO.setPageNo(pageNo);
        dashboardRequestParamsTO.setSize(size);
        dashboardRequestParamsTO.setSortBy(sortBy);
        dashboardRequestParamsTO.setUserGroupId(groupId);
        dashboardRequestParamsTO.setUserGroupStatus(userGroupStatus);
        if (groupId.equals(Group.SMC.getId())) {
            SMCProfile smcProfile = smcProfileRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCouncilId(smcProfile.getStateMedicalCouncil().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_DEAN.getId())) {
            CollegeDean collegeDean = collegeDeanRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCollegeId(collegeDean.getCollege().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())) {
            CollegeRegistrar collegeRegistrar = collegeRegistrarRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCollegeId(collegeRegistrar.getCollege().getId().toString());
        }
        if (groupId.equals(Group.COLLEGE_DEAN.getId()) || groupId.equals(Group.COLLEGE_REGISTRAR.getId()) || groupId.equals(Group.COLLEGE_ADMIN.getId())
                || groupId.equals(Group.NMC.getId()) || groupId.equals(Group.NBE.getId())) {
            dashboardRequestParamsTO.setSmcId(smcId);
        }
        final String sortingOrder = sortOrder == null ? DEFAULT_SORT_ORDER : sortOrder;
        dashboardRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = MAX_DATA_SIZE < size ? MAX_DATA_SIZE : size;
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
        User userDetail = userDetailRepository.findByUsername(userName);
        BigInteger groupId = userDetail.getGroup().getId();
        BigInteger userId = userDetail.getId();
        String sortOrder = dashboardRequestTO.getSortOrder();
        String column = getColumnToSort(dashboardRequestTO.getSortBy());
        int size = dashboardRequestTO.getSize();
        int pageNo = dashboardRequestTO.getPageNo();
        DashboardRequestParamsTO dashboardRequestParamsTO = new DashboardRequestParamsTO();
        dashboardRequestParamsTO.setWorkFlowStatusId(dashboardRequestTO.getWorkFlowStatusId());
        dashboardRequestParamsTO.setApplicationTypeId(dashboardRequestTO.getApplicationTypeId());
        dashboardRequestParamsTO.setName(dashboardRequestTO.getName());
        dashboardRequestParamsTO.setNmrId(dashboardRequestTO.getNmrId());
        dashboardRequestParamsTO.setSearch(dashboardRequestTO.getSearch());
        dashboardRequestParamsTO.setPageNo(pageNo);
        dashboardRequestParamsTO.setSize(size);
        dashboardRequestParamsTO.setSortBy(column);
        dashboardRequestParamsTO.setUserGroupId(groupId);
        dashboardRequestParamsTO.setUserGroupStatus(dashboardRequestTO.getUserGroupStatus());
        if (groupId.equals(Group.SMC.getId())) {
            SMCProfile smcProfile = smcProfileRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCouncilId(smcProfile.getStateMedicalCouncil().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_DEAN.getId())) {
            CollegeDean collegeDean = collegeDeanRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCollegeId(collegeDean.getCollege().getId().toString());
        } else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())) {
            CollegeRegistrar collegeRegistrar = collegeRegistrarRepository.findByUserId(userId);
            dashboardRequestParamsTO.setCollegeId(collegeRegistrar.getCollege().getId().toString());
        }
        if (groupId.equals(Group.COLLEGE_DEAN.getId()) || groupId.equals(Group.COLLEGE_REGISTRAR.getId()) || groupId.equals(Group.COLLEGE_ADMIN.getId())
                || groupId.equals(Group.NMC.getId()) || groupId.equals(Group.NBE.getId())) {
            dashboardRequestParamsTO.setSmcId(dashboardRequestTO.getSmcId());
        }
        final String sortingOrder = sortOrder == null ? DEFAULT_SORT_ORDER : sortOrder;
        dashboardRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = MAX_DATA_SIZE < size ? MAX_DATA_SIZE : size;
        Pageable pageable = PageRequest.of(pageNo, dataLimit);
        return iFetchSpecificDetailsCustomRepository.fetchDashboardData(dashboardRequestParamsTO, pageable);
    }

    /**
     * Maps the database column name to be used for sorting based on the columnToSort name.
     *
     * @param columnToSort - name of the column to be sorted
     * @return database column name to be used for sorting
     */
    private String getColumnToSort(String columnToSort) {
        Map<String, String> columns;
        if (columnToSort.length() > 0) {
            columns = mapColumnToTable();
            if (columns.containsKey(columnToSort)) {
                return columns.get(columnToSort);
            } else {
                return "Invalid column Name to sort";
            }
        } else {
            return " rd.created_at ";
        }
    }

    private Map<String, String> mapColumnToTable() {
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
        return columnToSortMap;
    }
}