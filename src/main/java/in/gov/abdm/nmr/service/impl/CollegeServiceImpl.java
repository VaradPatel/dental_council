package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.ICollegeMapper;
import in.gov.abdm.nmr.mapper.ICollegeMasterMapper;
import in.gov.abdm.nmr.repository.CollegeMasterRepository;
import in.gov.abdm.nmr.service.*;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
@Slf4j
public class CollegeServiceImpl implements ICollegeService, ICollegeMasterService {
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
    @Autowired
    private CollegeMasterRepository collegeMaster;
    @Autowired
    private ICollegeMasterMapper collegeMasterMapper;

    @Override
    public CollegeProfileTo registerCollege(BigInteger collegeId, CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException, WorkFlowException {
        CollegeProfileTo collegeCreationRequestToResponse = null;
        if (collegeId != null) {
            collegeRegistrationRequestTo.setId(collegeId);
        }
        if (!collegeRegistrationStatus()) {
            College collegeProfileEntity = collegeService.saveCollege(collegeRegistrationRequestTo, update);
            collegeCreationRequestToResponse = collegeMapper.collegeCreationRequestToResponse(collegeRegistrationRequestTo);
            collegeCreationRequestToResponse.setId(collegeProfileEntity.getId());
            collegeCreationRequestToResponse.setUserId(collegeProfileEntity.getUser().getId());
            if (collegeProfileEntity.getStateMedicalCouncil() != null) {
                collegeCreationRequestToResponse.setCouncilName(collegeProfileEntity.getStateMedicalCouncil().getName());
            }
            if (collegeProfileEntity.getState() != null) {
                collegeCreationRequestToResponse.setStateName(collegeProfileEntity.getState().getName());
            }
            if (collegeProfileEntity.getUniversity() != null) {
                collegeCreationRequestToResponse.setUniversityName(collegeProfileEntity.getUniversity().getName());
            }
            collegeCreationRequestToResponse.setApproved(collegeProfileEntity.isApproved());
            if ((!collegeProfileEntity.isApproved()) && (collegeRegistrationRequestTo.getRequestId() == null)) {
                String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.COLLEGE_REGISTRATION.getId()));
                collegeCreationRequestToResponse.setRequestId(requestId);
                collegeProfileEntity.setRequestId(requestId);
                workFlowService.initiateCollegeRegistrationWorkFlow(requestId, ApplicationType.COLLEGE_REGISTRATION.getId(), Group.COLLEGE_ADMIN.getId(), Action.SUBMIT.getId());
            }
        } else {
            throw new NmrException("College already exists", HttpStatus.BAD_REQUEST);
        }

        return collegeCreationRequestToResponse;
    }

    @Override
    public CollegeRegistrarProfileTo registerRegistrar(BigInteger collegeId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.saveCollegeRegistrar(collegeId, collegeRegistrarCreationRequestTo);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarRequestToResponse(collegeRegistrarCreationRequestTo);
        collegeRegistrarProfileTo.setId(collegeRegistrarEntity.getId());
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeRegistrarProfileTo updateRegisterRegistrar(BigInteger collegeId, BigInteger registrarId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.updateRegisterRegistrar(collegeId, registrarId, collegeRegistrarCreationRequestTo);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarRequestToResponse(collegeRegistrarCreationRequestTo);
        collegeRegistrarProfileTo.setId(collegeRegistrarEntity.getId());
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo registerDean(BigInteger collegeId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        CollegeDean collegeDeanEntity = collegeDeanDaoService.saveCollegeDean(collegeId, collegeDeanCreationRequestTo);
        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanRequestToResponse(collegeDeanCreationRequestTo);
        collegeDeanProfileTO.setId(collegeDeanEntity.getId());
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUser().getId());
        return collegeDeanProfileTO;
    }

    @Override
    public CollegeDeanProfileTo updateRegisterDean(BigInteger collegeId, BigInteger deanId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        CollegeDean collegeDeanEntity = collegeDeanDaoService.updateCollegeDean(collegeId, deanId, collegeDeanCreationRequestTo);
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
        collegeProfileTO.setUserId(collegeEntity.getUser().getId());
        return collegeProfileTO;
    }

    @Override
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId, BigInteger collegeId) throws NmrException {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.findCollegeRegistrarById(registrarId, collegeId);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarEntityToCollegeRegistrarProfile(collegeRegistrarEntity);
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo retrieveDeanProfile(BigInteger collegeId, BigInteger deanId) throws NmrException {
        CollegeDean collegeDeanEntity = collegeDeanDaoService.findCollegeDeanById(collegeId, deanId);
        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanEntityToCollegeDeanProfile(collegeDeanEntity);
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUser().getId());
        return collegeDeanProfileTO;
    }

    /**
     * Service Implementation's method for fetching the College registration records
     * for the NMC that has been submitted for approval
     *
     * @param pageNo       - Gives the current page number
     * @param limit        - Gives the number of records to be displayed
     * @param search
     * @param value
     * @param columnToSort -  According to which column the sort has to happen
     * @param sortOrder    -  Sorting order ASC or DESC
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the College submitted to NMC
     * for approval
     */
    @Override
    public CollegeRegistrationResponseTO getCollegeRegistrationDetails(String pageNo, String limit, String search, String value, String columnToSort, String sortOrder) throws InvalidRequestException {
        CollegeRegistrationResponseTO collegeRegistrationResponseTO = null;
        CollegeRegistrationRequestParamsTO collegeRegistrationRequestParams = new CollegeRegistrationRequestParamsTO();
        final Integer dataLimit = MAX_DATA_SIZE < Integer.valueOf(limit) ? MAX_DATA_SIZE : Integer.valueOf(limit);
        collegeRegistrationRequestParams.setOffset(dataLimit);
        collegeRegistrationRequestParams.setPageNo(Integer.valueOf(pageNo));
        if (StringUtils.isNotBlank(search)) {
            switch (search.toLowerCase()) {
                case COLLEGE_ID_IN_LOWER_CASE:
                    collegeRegistrationRequestParams.setCollegeId(value);
                    break;
                case COLLEGE_NAME_IN_LOWER_CASE:
                    collegeRegistrationRequestParams.setCollegeName(value);
                    break;
                case COUNCIL_NAME_IN_LOWER_CASE:
                    collegeRegistrationRequestParams.setCouncilName(value);
                    break;
                case SEARCH_IN_LOWER_CASE:
                    collegeRegistrationRequestParams.setSearch(value);
                    break;
                default:
                    throw new InvalidRequestException(INVALID_SEARCH_CRITERIA_FOR_COLLEGE_APPLICATIONS);
            }
        }
        String column = getColumnToSort(columnToSort);
        collegeRegistrationRequestParams.setSortBy(column);
        final String sortingOrder = sortOrder == null ? DEFAULT_SORT_ORDER : sortOrder;
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

    private boolean collegeRegistrationStatus() {
        return false;
    }

    @Override
    public List<CollegeMasterTo> getCollegesByStateId(BigInteger stateId) {
        return collegeMasterMapper.collegeMasterTo(collegeMaster.getCollegesByStateId(stateId));
    }
}
