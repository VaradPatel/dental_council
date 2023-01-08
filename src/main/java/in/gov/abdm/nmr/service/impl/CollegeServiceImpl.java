package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.*;
import in.gov.abdm.nmr.util.NMRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.dto.CollegeDeanProfileTo;
import in.gov.abdm.nmr.dto.CollegeProfileTo;
import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.dto.CollegeRegistrarProfileTo;
import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.mapper.ICollegeMapper;
import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.NmrException;

import javax.persistence.Id;

@Service
public class CollegeServiceImpl implements ICollegeService {

    @Autowired
    private ICollegeDaoService collegeService;

    @Autowired
    private ICollegeMapper collegeMapper;

    @Autowired
    private ICollegeRegistrarDaoService collegeRegistrarDaoService;

    @Autowired
    private ICollegeDeanDaoService collegeDeanDaoService;

    @Autowired
    private IUserDaoService userDetailService;

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
        if(collegeRegistrationRequestTo.getRequestId() == null){
            String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.COLLEGE_REGISTRATION.getId()));
            collegeCreationRequestToResponse.setRequestId(requestId);
            collegeProfileEntity.setRequestId(requestId);
            workFlowService.initiateCollegeRegistrationWorkFlow(requestId,ApplicationType.COLLEGE_REGISTRATION.getId(), Group.COLLEGE_ADMIN.getId(), Action.SUBMIT.getId());
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
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User collegeUserDetail = userDetailService.findUserDetailByUsername(userName);
        College collegeEntity = collegeService.findById(collegeId);
        if (!collegeUserDetail.getId().equals(collegeEntity.getUser().getId())) {
            throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
        }

        CollegeProfileTo collegeProfileTO = collegeMapper.collegeEntityToCollegeProfile(collegeEntity);
        collegeProfileTO.setCouncilId(collegeEntity.getStateMedicalCouncil().getId());
//        collegeProfileTO.setStateId(collegeEntity.getState().getId());
        collegeProfileTO.setUniversityId(collegeEntity.getUniversity().getId());
        return collegeProfileTO;
    }

    @Override
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId) throws NmrException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User collegeUserDetail = userDetailService.findUserDetailByUsername(userName);
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.findCollegeRegistrarById(registrarId);
        if (!collegeUserDetail.getId().equals(collegeRegistrarEntity.getUser().getId())) {
            throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
        }

        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarEntityToCollegeRegistrarProfile(collegeRegistrarEntity);
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo retrieveDeanProfile(BigInteger id) throws NmrException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User collegeUserDetail = userDetailService.findUserDetailByUsername(userName);
        CollegeDean collegeDeanEntity = collegeDeanDaoService.findCollegeDeanById(id);
        if (!collegeUserDetail.getId().equals(collegeDeanEntity.getUser().getId())) {
            throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
        }

        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanEntityToCollegeDeanProfile(collegeDeanEntity);
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUser().getId());
        return collegeDeanProfileTO;
    }
}
