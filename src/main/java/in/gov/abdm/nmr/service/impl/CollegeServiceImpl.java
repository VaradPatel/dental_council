package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.ICollegeMapper;
import in.gov.abdm.nmr.service.*;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CollegeServiceImpl implements ICollegeService {
    @Value("${max.data.size}")
    private Integer maxSize;
    @Value("${sort.order}")
    private String defaultSortOrder;
    @Autowired
    private ICollegeDaoService collegeService;

    @Autowired
    private ICollegeMapper collegeMapper;

    @Autowired
    private ICollegeRegistrarDaoService collegeRegistrarDaoService;

    @Autowired
    private ICollegeDeanDaoService collegeDeanDaoService;

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired
    private IRequestCounterService requestCounterService;

    @Override
    public CollegeProfileTo registerCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException, WorkFlowException {
        College collegeProfileEntity = collegeService.saveCollege(collegeRegistrationRequestTo, update);
        CollegeProfileTo collegeCreationRequestToResponse = collegeMapper.collegeCreationRequestToResponse(collegeRegistrationRequestTo);
        collegeCreationRequestToResponse.setId(collegeProfileEntity.getId());
        collegeCreationRequestToResponse.setUserId(collegeProfileEntity.getUser().getId());
        collegeCreationRequestToResponse.setCouncilName(collegeProfileEntity.getStateMedicalCouncil().getName());
        collegeCreationRequestToResponse.setStateName(collegeProfileEntity.getState().getName());
        collegeCreationRequestToResponse.setUniversityName(collegeProfileEntity.getUniversity().getName());
        if (collegeRegistrationRequestTo.getRequestId() == null) {
            String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.COLLEGE_REGISTRATION.getId()));
            collegeCreationRequestToResponse.setRequestId(requestId);
            collegeProfileEntity.setRequestId(requestId);
            workFlowService.initiateCollegeRegistrationWorkFlow(requestId, ApplicationType.COLLEGE_REGISTRATION.getId(), Group.COLLEGE_ADMIN.getId(), Action.SUBMIT.getId());
        }
        return collegeCreationRequestToResponse;
    }

    @Override
    public CollegeRegistrarProfileTo registerRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.saveCollegeRegistrar(collegeRegistrarCreationRequestTo);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarRequestToResponse(collegeRegistrarCreationRequestTo);
        collegeRegistrarProfileTo.setId(collegeRegistrarEntity.getId());
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo registerDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        CollegeDean collegeDeanEntity = collegeDeanDaoService.saveCollegeDean(collegeDeanCreationRequestTo);
        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanRequestToResponse(collegeDeanCreationRequestTo);
        collegeDeanProfileTO.setId(collegeDeanEntity.getId());
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUser().getId());
        return collegeDeanProfileTO;
    }

    @Override
    public CollegeProfileTo retrieveCollegeProfile(BigInteger collegeId) throws NmrException {
        College collegeEntity = collegeService.findById(collegeId);
        CollegeProfileTo collegeProfileTO = collegeMapper.collegeEntityToCollegeProfile(collegeEntity);
        collegeProfileTO.setCouncilName(collegeEntity.getStateMedicalCouncil().getName());
        collegeProfileTO.setStateName(collegeEntity.getState().getName());
        collegeProfileTO.setUniversityName(collegeEntity.getUniversity().getName());
        return collegeProfileTO;
    }

    @Override
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId) throws NmrException {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.findCollegeRegistrarById(registrarId);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarEntityToCollegeRegistrarProfile(collegeRegistrarEntity);
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo retrieveDeanProfile(BigInteger id) throws NmrException {
        CollegeDean collegeDeanEntity = collegeDeanDaoService.findCollegeDeanById(id);
        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanEntityToCollegeDeanProfile(collegeDeanEntity);
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUser().getId());
        return collegeDeanProfileTO;
    }

    /**
     * Service Implementation's method for fetching the College registration records
     * for the NMC that has been submitted for approval
     *
     * @param pageNo       - Gives the current page number
     * @param offset        - Gives the number of records to be displayed
     * @param search       - Gives the search criteria like HP_Id, HP_name, Submiited_Date, Remarks
     * @param sortBy -  According to which column the sort has to happen
     * @param sortType    -  Sorting order ASC or DESC
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the College submitted to NMC
     * for approval
     */
    @Override
    public CollegeRegistrationResponseTO getCollegeRegistrationDetails(String pageNo, String offset, String search, String collegeId, String collegeName, String councilName, String sortBy, String sortType) {
        CollegeRegistrationResponseTO collegeRegistrationResponseTO = null;
        CollegeRegistrationRequestParamsTO collegeRegistrationRequestParams = new CollegeRegistrationRequestParamsTO();
        final Integer dataLimit = maxSize < Integer.valueOf(offset) ? maxSize : Integer.valueOf(offset);
        collegeRegistrationRequestParams.setOffset(dataLimit);
        collegeRegistrationRequestParams.setPageNo(Integer.valueOf(pageNo));
        collegeRegistrationRequestParams.setCollegeId(collegeId);
        collegeRegistrationRequestParams.setCollegeName(collegeName);
        collegeRegistrationRequestParams.setCouncilName(councilName);
        collegeRegistrationRequestParams.setSearch(search);
        String column = getColumnToSort(sortBy);
        collegeRegistrationRequestParams.setSortBy(column);
        final String sortingOrder = sortType == null ? defaultSortOrder : sortType;
        collegeRegistrationRequestParams.setSortType(sortingOrder);
        try {
            Pageable pageable = PageRequest.of(collegeRegistrationRequestParams.getPageNo(), collegeRegistrationRequestParams.getOffset());
            collegeRegistrationResponseTO = collegeService.getCollegeRegistrationData(collegeRegistrationRequestParams, pageable);
        } catch (Exception e) {
            log.error("Service exception " + e.getMessage());
        }
        return collegeRegistrationResponseTO;
    }

    private String getColumnToSort(String columnToSort) {
        Map<String, String> columns;
        if (columnToSort != null && columnToSort.length() > 0) {
            columns = mapColumnToTable();
            if (columns.containsKey(columnToSort)) {
                return columns.get(columnToSort);
            } else {
                return " wf.created_at ";
            }
        } else {
            return " wf.created_at ";
        }
    }

    private Map<String, String> mapColumnToTable() {
        Map<String, String> columnToSortMap = new HashMap<>();
        columnToSortMap.put("createdAt", " wf.created_at");
        columnToSortMap.put("collegeId", " c.college_code");
        columnToSortMap.put("collegeName", " c.name");
        columnToSortMap.put("councilName", " smc.name");
        columnToSortMap.put("status", "  wfs.name");
        columnToSortMap.put("pendency", " pendency");
        return columnToSortMap;
    }
}
