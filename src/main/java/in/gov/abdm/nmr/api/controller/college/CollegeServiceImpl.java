package in.gov.abdm.nmr.api.controller.college;

import java.math.BigInteger;

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

@Service
public class CollegeServiceImpl implements ICollegeService {

    private ICollegeDaoService collegeService;

    private ICollegeMapper collegeMapper;

    private ICollegeRegistrarDaoService collegeRegistrarDaoService;
    
    private ICollegeDeanDaoService collegeDeanDaoService;

    public CollegeServiceImpl(ICollegeDaoService collegeService, ICollegeMapper collegeMapper, ICollegeRegistrarDaoService collegeRegistrarDaoService, ICollegeDeanDaoService collegeDeanDaoService) {
        super();
        this.collegeService = collegeService;
        this.collegeMapper = collegeMapper;
        this.collegeRegistrarDaoService = collegeRegistrarDaoService;
        this.collegeDeanDaoService = collegeDeanDaoService;
    }

    @Override
    public CollegeProfileTo registerCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo) {
        College collegeProfileEntity = collegeService.saveCollege(collegeRegistrationRequestTo);
        CollegeProfileTo collegeCreationRequestToResponse = collegeMapper.collegeCreationRequestToResponse(collegeRegistrationRequestTo);
        collegeCreationRequestToResponse.setId(collegeProfileEntity.getId());
        collegeCreationRequestToResponse.setUserId(collegeProfileEntity.getUserDetail().getId());
        return collegeCreationRequestToResponse;
    }

    @Override
    public CollegeRegistrarProfileTo registerRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.saveCollegeRegistrar(collegeRegistrarCreationRequestTo);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarRequestToResponse(collegeRegistrarCreationRequestTo);
        collegeRegistrarProfileTo.setId(collegeRegistrarEntity.getId());
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUserDetail().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo registerDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) {
        CollegeDean collegeDeanEntity  = collegeDeanDaoService.saveCollegeDean(collegeDeanCreationRequestTo);
        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanRequestToResponse(collegeDeanCreationRequestTo);
        collegeDeanProfileTO.setId(collegeDeanEntity.getId());
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUserDetail().getId());
        return collegeDeanProfileTO;
    }

    @Override
    public CollegeProfileTo retrieveCollegeProfile(BigInteger collegeId) {
        College collegeEntity = collegeService.findById(collegeId);
        CollegeProfileTo collegeProfileTO = collegeMapper.collegeEntityToCollegeProfile(collegeEntity);
        collegeProfileTO.setCouncilId(collegeEntity.getStateMedicalCouncil().getId());
        collegeProfileTO.setStateId(collegeEntity.getState().getId());
        collegeProfileTO.setUniversityId(collegeEntity.getUniversity().getId());
        return collegeProfileTO;
    }

    @Override
    public CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId) {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarDaoService.findCollegeRegistrarById(registrarId);
        CollegeRegistrarProfileTo collegeRegistrarProfileTo = collegeMapper.collegeRegistrarEntityToCollegeRegistrarProfile(collegeRegistrarEntity);
        collegeRegistrarProfileTo.setUserId(collegeRegistrarEntity.getUserDetail().getId());
        return collegeRegistrarProfileTo;
    }

    @Override
    public CollegeDeanProfileTo retrieveDeanProfile(BigInteger id) {
        CollegeDean collegeDeanEntity = collegeDeanDaoService.findCollegeDeanById(id);
        CollegeDeanProfileTo collegeDeanProfileTO = collegeMapper.collegeDeanEntityToCollegeDeanProfile(collegeDeanEntity);
        collegeDeanProfileTO.setUserId(collegeDeanEntity.getUserDetail().getId());
        return collegeDeanProfileTO;
    }
}
