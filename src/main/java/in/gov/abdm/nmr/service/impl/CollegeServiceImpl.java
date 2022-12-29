package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import in.gov.abdm.nmr.service.ICollegeService;
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
import in.gov.abdm.nmr.service.ICollegeDaoService;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.service.ICollegeDeanDaoService;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.service.ICollegeRegistrarDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.NmrException;

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
    public CollegeProfileTo registerCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException {
        College collegeProfileEntity = collegeService.saveCollege(collegeRegistrationRequestTo, update);
        CollegeProfileTo collegeCreationRequestToResponse = collegeMapper.collegeCreationRequestToResponse(collegeRegistrationRequestTo);
        collegeCreationRequestToResponse.setId(collegeProfileEntity.getId());
        collegeCreationRequestToResponse.setUserId(collegeProfileEntity.getUser().getId());
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
        collegeProfileTO.setStateId(collegeEntity.getState().getId());
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
