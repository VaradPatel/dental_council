package in.gov.abdm.nmr.api.controller.college;

import java.math.BigInteger;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.ICollegeMapper;
import in.gov.abdm.nmr.db.sql.domain.college.College;
import in.gov.abdm.nmr.db.sql.domain.college.ICollegeDaoService;
import in.gov.abdm.nmr.db.sql.domain.college_dean.CollegeDean;
import in.gov.abdm.nmr.db.sql.domain.college_dean.ICollegeDeanDaoService;
import in.gov.abdm.nmr.db.sql.domain.college_registrar.CollegeRegistrar;
import in.gov.abdm.nmr.db.sql.domain.college_registrar.ICollegeRegistrarDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.IUserDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.User;

@Service
public class CollegeServiceImpl implements ICollegeService {

    private ICollegeDaoService collegeService;

    private ICollegeMapper collegeMapper;

    private ICollegeRegistrarDaoService collegeRegistrarDaoService;

    private ICollegeDeanDaoService collegeDeanDaoService;

    private IUserDaoService userDetailService;

    public CollegeServiceImpl(ICollegeDaoService collegeService, ICollegeMapper collegeMapper, ICollegeRegistrarDaoService collegeRegistrarDaoService, ICollegeDeanDaoService collegeDeanDaoService, IUserDaoService userDetailService) {
        this.collegeService = collegeService;
        this.collegeMapper = collegeMapper;
        this.collegeRegistrarDaoService = collegeRegistrarDaoService;
        this.collegeDeanDaoService = collegeDeanDaoService;
        this.userDetailService = userDetailService;
    }

    @Override
    public CollegeProfileTo registerCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) {
        College collegeProfileEntity = collegeService.saveCollege(collegeRegistrationRequestTo, update);
        CollegeProfileTo collegeCreationRequestToResponse = collegeMapper.collegeCreationRequestToResponse(collegeRegistrationRequestTo);
        collegeCreationRequestToResponse.setId(collegeProfileEntity.getId());
        collegeCreationRequestToResponse.setUserId(collegeProfileEntity.getUser().getId());
        return collegeCreationRequestToResponse;
    }

    @Override
    public CollegeRegistrarProfileTo registerRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.saveCollegeRegistrar(collegeRegistrarCreationRequestTo);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarRequestToResponse(collegeRegistrarCreationRequestTo);
        collegeRegistrarProfileTo.setId(collegeRegistrarEntity.getId());
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo registerDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) {
        CollegeDean collegeDeanEntity = collegeDeanDaoService.saveCollegeDean(collegeDeanCreationRequestTo);
        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanRequestToResponse(collegeDeanCreationRequestTo);
        collegeDeanProfileTO.setId(collegeDeanEntity.getId());
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUser().getId());
        return collegeDeanProfileTO;
    }

    @Override
    public CollegeProfileTo retrieveCollegeProfile(BigInteger collegeId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User collegeUserDetail = userDetailService.findUserDetailByUsername(userName);
        College collegeEntity = collegeService.findById(collegeId);
        if (!collegeUserDetail.getId().equals(collegeEntity.getUser().getId())) {
            throw new AuthenticationServiceException("Forbidden");
        }

        CollegeProfileTo collegeProfileTO = collegeMapper.collegeEntityToCollegeProfile(collegeEntity);
        collegeProfileTO.setCouncilId(collegeEntity.getStateMedicalCouncil().getId());
        collegeProfileTO.setStateId(collegeEntity.getState().getId());
        collegeProfileTO.setUniversityId(collegeEntity.getUniversity().getId());
        return collegeProfileTO;
    }

    @Override
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User collegeUserDetail = userDetailService.findUserDetailByUsername(userName);
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.findCollegeRegistrarById(registrarId);
        if (!collegeUserDetail.getId().equals(collegeRegistrarEntity.getUser().getId())) {
            throw new AuthenticationServiceException("Forbidden");
        }

        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarEntityToCollegeRegistrarProfile(collegeRegistrarEntity);
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo retrieveDeanProfile(BigInteger id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User collegeUserDetail = userDetailService.findUserDetailByUsername(userName);
        CollegeDean collegeDeanEntity = collegeDeanDaoService.findCollegeDeanById(id);
        if (!collegeUserDetail.getId().equals(collegeDeanEntity.getUser().getId())) {
            throw new AuthenticationServiceException("Forbidden");
        }

        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanEntityToCollegeDeanProfile(collegeDeanEntity);
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUser().getId());
        return collegeDeanProfileTO;
    }
}
