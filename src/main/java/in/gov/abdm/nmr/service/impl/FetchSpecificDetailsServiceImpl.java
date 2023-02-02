package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@Service
public class FetchSpecificDetailsServiceImpl implements IFetchSpecificDetailsService {

    @Value("${max.data.size}")
    private Integer maxSize;
    @Value("${sort.order}")
    private String defaultSortOrder;

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

    @Autowired
    private IFetchSpecificDetailsCustomRepository iFetchSpecificDetailsCustomRepository;

    @Autowired
    private IUserRepository userDetailRepository;

    @Autowired
    private ISmcProfileRepository smcProfileRepository;

    @Autowired
    private ICollegeDeanRepository collegeDeanRepository;

    @Autowired
    private ICollegeRegistrarRepository collegeRegistrarRepository;

    @Override
    public List<FetchSpecificDetailsResponseTO> fetchSpecificDetails(String groupName, String applicationType, String workFlowStatus) throws InvalidRequestException {
        validateGroupName(groupName);
        validateApplicationType(applicationType);
        validateWorkFlowStatus(workFlowStatus);

        return fetchDetailsForListingByStatus(groupName, applicationType, workFlowStatus)
                .stream()
                .map(fetchSpecificDetails-> {
                    FetchSpecificDetailsResponseTO fetchSpecificDetailsResponseTO=iFetchSpecificDetailsMapper.toFetchSpecificDetailsResponseTO(fetchSpecificDetails);

                    if(Group.COLLEGE_ADMIN.getDescription().equals(fetchSpecificDetails.getGroupName()) ||
                            Group.COLLEGE_DEAN.getDescription().equals(fetchSpecificDetails.getGroupName()) ||
                            Group.COLLEGE_REGISTRAR.getDescription().equals(fetchSpecificDetails.getGroupName())){
                        fetchSpecificDetailsResponseTO.setCollegeVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if(Group.SMC.getDescription().equals(fetchSpecificDetails.getGroupName())){
                        fetchSpecificDetailsResponseTO.setCouncilVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if(Group.NMC.getDescription().equals(fetchSpecificDetails.getGroupName())){
                        fetchSpecificDetailsResponseTO.setNMCVerificationStatus(fetchSpecificDetails.getWorkFlowStatus());
                    }
                    if(fetchSpecificDetails.getDateOfSubmission()!=null) {
                        long diffInMillis = Math.abs(new Date().getTime() - fetchSpecificDetails.getDateOfSubmission().getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                        fetchSpecificDetailsResponseTO.setPendency(BigInteger.valueOf(diff));
                    }
                    return fetchSpecificDetailsResponseTO;
                })
                .toList();
    }

    private List<IFetchSpecificDetails> fetchDetailsForListingByStatus(String groupName, String applicationType, String workFlowStatus){
        if(WorkflowStatus.APPROVED.getDescription().equals(workFlowStatus)){
            return iFetchSpecificDetailsRepository.fetchDetailsWithApprovedStatusForListing(groupName,applicationType,workFlowStatus);
        } else if (WorkflowStatus.PENDING.getDescription().equals(workFlowStatus)) {
            return iFetchSpecificDetailsRepository.fetchDetailsWithPendingStatusForListing(groupName,applicationType,workFlowStatus);
        }
        return iFetchSpecificDetailsRepository.fetchDetailsForListing(groupName,applicationType,workFlowStatus);
    }
    private void validateGroupName(String groupName) throws InvalidRequestException {
        if(groupName==null || Arrays.stream(Group.values()).noneMatch(t -> t.getDescription().equals(groupName))){
            throw new InvalidRequestException(INVALID_GROUP);
        }
    }

    private void validateApplicationType(String applicationType) throws InvalidRequestException {
        if(applicationType==null || Arrays.stream(ApplicationType.values()).noneMatch(t -> t.getDescription().equals(applicationType))){
            throw new InvalidRequestException(INVALID_APPLICATION_TYPE);
        }
    }

    private void validateWorkFlowStatus(String workFlowStatus) throws InvalidRequestException {
        if(workFlowStatus==null || Arrays.stream(WorkflowStatus.values()).noneMatch(t -> t.getDescription().equals(workFlowStatus))){
            throw new InvalidRequestException(INVALID_WORK_FLOW_STATUS);
        }
    }

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

        if(groupId.equals(Group.SMC.getId())){
            SMCProfile smcProfile = smcProfileRepository.findByUserDetail(userId);
            dashboardRequestParamsTO.setSmcId(smcProfile.getStateMedicalCouncil().getId().toString());
        }else if (groupId.equals(Group.COLLEGE_DEAN.getId())){
            CollegeDean collegeDean = collegeDeanRepository.findByUserDetail(userId);
            dashboardRequestParamsTO.setCollegeId(collegeDean.getCollege().getId().toString());
        }else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())){
            CollegeRegistrar collegeRegistrar = collegeRegistrarRepository.findByUserDetail(userId);
            dashboardRequestParamsTO.setCollegeId(collegeRegistrar.getCollege().getId().toString());
        }

        final String sortingOrder = sortOrder == null ? defaultSortOrder : sortOrder;
        dashboardRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = maxSize < size ? maxSize : size;
        Pageable pageable = PageRequest.of(pageNo, dataLimit);
        return iFetchSpecificDetailsCustomRepository.fetchDashboardData(dashboardRequestParamsTO, pageable);
    }

    @Override
    public List<FetchTrackApplicationResponseTO> fetchTrackApplicationDetails(FetchTrackApplicationRequestTO requestTO) {
        List<FetchTrackApplicationResponseTO> list = new ArrayList<>();
        List<BigInteger> searchByAppType;
        Pageable pagination = PageRequest.of(
                requestTO.getPage(),
                requestTO.getSize() > maxSize ? maxSize : requestTO.getSize(),
                Sort.by(requestTO.getSortBy()).ascending());
        if (Objects.nonNull(requestTO.getApplicationType()) && !requestTO.getApplicationType().equalsIgnoreCase("")) {
            searchByAppType = Arrays.asList(Arrays.stream(ApplicationType.values())
                    .filter(applicationType -> requestTO.getApplicationType().equals(applicationType.getDescription()))
                    .findFirst().get().getId());
        } else {
            searchByAppType = Arrays.asList(ApplicationType.HP_REGISTRATION.getId(),
                    ApplicationType.HP_MODIFICATION.getId(),
                    ApplicationType.HP_TEMPORARY_SUSPENSION.getId(),
                    ApplicationType.HP_PERMANENT_SUSPENSION.getId(),
                    ApplicationType.HP_ACTIVATE_LICENSE.getId(),
                    ApplicationType.COLLEGE_REGISTRATION.getId(),
                    ApplicationType.FOREIGN_HP_REGISTRATION.getId(),
                    ApplicationType.QUALIFICATION_ADDITION.getId(),
                    ApplicationType.FOREIGN_HP_MODIFICATION.getId());
        }

        List<Tuple> queryResult = iFetchSpecificDetailsRepository.fetchTrackApplicationDetails(
                iFetchSpecificDetailsRepository.getHpProfileIds(requestTO.getHpId()), searchByAppType, pagination);

        FetchTrackApplicationResponseTO response;
        for (Tuple tuple : queryResult) {
            response = new FetchTrackApplicationResponseTO();
            response.setRequestId(tuple.get("request_id", String.class));
            response.setApplicationTypeId(Arrays.stream(ApplicationType.values())
                    .filter(appType -> tuple.get("application_type_id", BigInteger.class).equals(appType.getId()))
                    .findFirst().get().getDescription().toString());
            response.setCreatedAt(tuple.get("created_at", Date.class));
            response.setWorkFlowStatusId(tuple.get("work_flow_status_id", BigInteger.class));
            response.setCurrentGroupId(tuple.get("current_group_id", BigInteger.class));
            response.setPendencyDays(tuple.get("pendency_days", BigDecimal.class));
            list.add(response);
        }
        return list;
    }

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
        columnToSortMap.put("doctor", " doctor");
        columnToSortMap.put("smc", " smc");
        columnToSortMap.put("collegeDean", " college_dean");
        columnToSortMap.put("collegeRegistrar", " college_registrar");
        columnToSortMap.put("nmc", " nmc");
        columnToSortMap.put("hpProfileId", " hp_profile_id");
        columnToSortMap.put("requestId", " request_id");
        columnToSortMap.put("registrationNo", " registration_no");
        columnToSortMap.put("createdAt", " created_at");
        columnToSortMap.put("name", " name");
        columnToSortMap.put("fullName", " full_name");
        return columnToSortMap;
    }
}