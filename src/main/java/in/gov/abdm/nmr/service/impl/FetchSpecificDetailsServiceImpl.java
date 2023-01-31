package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardRequestTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetailsMapper;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsCustomRepository;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsRepository;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        String workFlowStatus = dashboardRequestTO.getWorkFlowStatus();
        String sortOrder = dashboardRequestTO.getSortOrder();
        String column = getColumnToSort(dashboardRequestTO.getSortBy());
        int size = dashboardRequestTO.getSize();
        int pageNo = dashboardRequestTO.getPageNo();
        validateWorkFlowStatus(workFlowStatus);
        String workFlowStatusId = Arrays.stream(WorkflowStatus.values()).filter(workFlow -> workFlowStatus.equals(workFlow.getDescription())).findFirst().get().getId().toString();
        DashboardRequestParamsTO dashboardRequestParamsTO = new DashboardRequestParamsTO();
        dashboardRequestParamsTO.setWorkFlowStatusId(workFlowStatusId);
        dashboardRequestParamsTO.setName(dashboardRequestTO.getName());
        dashboardRequestParamsTO.setNmrId(dashboardRequestTO.getNmrId());
        dashboardRequestParamsTO.setSearch(dashboardRequestTO.getSearch());
        dashboardRequestParamsTO.setPageNo(pageNo);
        dashboardRequestParamsTO.setSize(size);
        dashboardRequestParamsTO.setSortBy(column);

        final String sortingOrder = sortOrder == null ? defaultSortOrder : sortOrder;
        dashboardRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = maxSize < size ? maxSize : size;
        Pageable pageable = PageRequest.of(pageNo, dataLimit);
        return iFetchSpecificDetailsCustomRepository.fetchDashboardData(dashboardRequestParamsTO, pageable);
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
        columnToSortMap.put("collegeDean", " College_Dean");
        columnToSortMap.put("collegeRegistrar", " College_Registrar");
        columnToSortMap.put("nmc", " nmc");
        columnToSortMap.put("hpProfileId", " calculate.hp_profile_id");
        columnToSortMap.put("requestId", " calculate.request_id");
        columnToSortMap.put("createdAt", " rd.created_at");
        columnToSortMap.put("name", " stmc.name");
        columnToSortMap.put("fullName", " hp.full_name");
        return columnToSortMap;
    }
}
