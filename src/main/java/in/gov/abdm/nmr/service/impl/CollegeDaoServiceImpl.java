package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestParamsTO;
import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.dto.CollegeRegistrationResponseTO;
import in.gov.abdm.nmr.dto.GetSetPasswordLinkTo;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.ICollegeDtoMapper;
import in.gov.abdm.nmr.repository.ICollegeRepository;
import in.gov.abdm.nmr.repository.ICollegeRepositoryCustom;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
@Transactional
public class CollegeDaoServiceImpl implements ICollegeDaoService {

    private ICollegeRepository collegeRepository;

    private ICollegeDtoMapper collegeDtoMapper;

    private EntityManager entityManager;

    private IUserDaoService userDetailService;

    private IAccessControlService accessControlService;

    @Autowired
    private ICollegeRepositoryCustom collegeRepositoryCustom;
    @Autowired
    private PasswordServiceImpl passwordReset;

    public CollegeDaoServiceImpl(ICollegeRepository collegeRepository, ICollegeDtoMapper collegeDtoMapper, EntityManager entityManager, IUserDaoService userDetailService, //
                                 IAccessControlService accessControlService) {
        this.collegeRepository = collegeRepository;
        this.collegeDtoMapper = collegeDtoMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.accessControlService = accessControlService;
    }

    @Override
    public List<CollegeTO> getCollegeData(BigInteger universityId) {
        return collegeDtoMapper.collegeDataToDto(collegeRepository.getCollege(universityId));
    }

    @Override
    public College saveCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException {
        if (!update) {
            if (userDetailService.findByUsername(collegeRegistrationRequestTo.getEmailId()) != null) {
                throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }

            User userDetail = new User(null, collegeRegistrationRequestTo.getEmailId(), null, null, true, true, //
                    entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserGroup.class, in.gov.abdm.nmr.enums.Group.COLLEGE_REGISTRAR.getId()), true, 0, null);
            userDetailService.saveUserDetail(userDetail);

            College collegeEntity = collegeDtoMapper.collegeRegistartionDtoToEntity(collegeRegistrationRequestTo);
            collegeEntity.setId(null);
            collegeEntity.setUser(userDetail);

            collegeEntity.setState(entityManager.getReference(State.class, collegeRegistrationRequestTo.getStateId()));
            collegeEntity.setStateMedicalCouncil(entityManager.getReference(StateMedicalCouncil.class, collegeRegistrationRequestTo.getCouncilId()));
            collegeEntity.setUniversity(entityManager.getReference(University.class, collegeRegistrationRequestTo.getUniversityId()));
            collegeEntity.setApproved(collegeRegistrationRequestTo.isApproved());
            College college = collegeRepository.saveAndFlush(collegeEntity);

            GetSetPasswordLinkTo setPasswordLinkTo = new GetSetPasswordLinkTo();
            setPasswordLinkTo.setUsername(String.valueOf(college.getId()));
            setPasswordLinkTo.setEmail(college.getEmailId());
            setPasswordLinkTo.setMobile(college.getPhoneNumber());
            passwordReset.passwordReset(setPasswordLinkTo);

            return college;
        } else {
            College collegeEntity = collegeRepository.findByUserId(collegeRegistrationRequestTo.getUserId());
            if (collegeEntity == null) {
                throw new NmrException("Invalid college", HttpStatus.BAD_REQUEST);
            }
            if (!collegeEntity.getId().equals(collegeRegistrationRequestTo.getId())) {
                throw new NmrException(FORBIDDEN, HttpStatus.FORBIDDEN);
            }

            User collegeUserDetail = userDetailService.findById(collegeRegistrationRequestTo.getUserId());
            collegeUserDetail.setUsername(collegeRegistrationRequestTo.getEmailId());
            userDetailService.saveUserDetail(collegeUserDetail);

            Timestamp createdAt = collegeEntity.getCreatedAt();
            collegeEntity = collegeDtoMapper.collegeRegistartionDtoToEntity(collegeRegistrationRequestTo);
            collegeEntity.setCreatedAt(createdAt);
            collegeEntity.setUser(collegeUserDetail);
            collegeEntity.setState(entityManager.getReference(State.class, collegeRegistrationRequestTo.getStateId()));
            collegeEntity.setStateMedicalCouncil(entityManager.getReference(StateMedicalCouncil.class, collegeRegistrationRequestTo.getCouncilId()));
            collegeEntity.setUniversity(entityManager.getReference(University.class, collegeRegistrationRequestTo.getUniversityId()));

            return collegeRepository.saveAndFlush(collegeEntity);
        }
    }

    @Override
    public College findById(BigInteger collegeId) throws NmrException {
        College collegeEntity = collegeRepository.findById(collegeId).orElse(null);
        if (collegeEntity == null) {
            throw new NmrException(INVALID_COLLEGE_ID, HttpStatus.BAD_REQUEST);
        }
        return collegeEntity;
    }

    @Override
    public College findByUserId(BigInteger userId) {
        return collegeRepository.findByUserId(userId);
    }

    /**
     * Service Implementation's method  for fetching the College registration records
     * for the NMC that has been submitted for approval
     *
     * @param collegeRegistrationRequestParamsTO - Object with all the attributes related to pagination, filter and sorting
     * @param pageable                           - Object of Pageable that helps in pagination
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the College submitted to NMC
     * for approval
     */
    @Override
    public CollegeRegistrationResponseTO getCollegeRegistrationData(CollegeRegistrationRequestParamsTO collegeRegistrationRequestParamsTO, Pageable pageable) {
        return collegeRepositoryCustom.getCollegeRegistrationData(collegeRegistrationRequestParamsTO, pageable);
    }
}
