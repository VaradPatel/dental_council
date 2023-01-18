package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import in.gov.abdm.nmr.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.ICollegeDtoMapper;
import in.gov.abdm.nmr.repository.ICollegeRepository;
import in.gov.abdm.nmr.service.ICollegeDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;

@Service
@Transactional
public class CollegeDaoServiceImpl implements ICollegeDaoService {

    private ICollegeRepository collegeRepository;

    private ICollegeDtoMapper collegeDtoMapper;

    private EntityManager entityManager;

    private IUserDaoService userDetailService;

    public CollegeDaoServiceImpl(ICollegeRepository collegeRepository, ICollegeDtoMapper collegeDtoMapper, EntityManager entityManager, IUserDaoService userDetailService) {
        this.collegeRepository = collegeRepository;
        this.collegeDtoMapper = collegeDtoMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
    }

    @Override
    public List<CollegeTO> getCollegeData(BigInteger universityId) {
        return collegeDtoMapper.collegeDataToDto(collegeRepository.getCollege(universityId));
    }

    @Override
    public College saveCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException {
        if (!update) {
            if (userDetailService.findUserDetailByUsername(collegeRegistrationRequestTo.getEmailId()) != null) {
                throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
            }

            User userDetail = new User(null, collegeRegistrationRequestTo.getEmailId(), null, null, true, true, //
                    entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()),  entityManager.getReference(Group.class, in.gov.abdm.nmr.enums.Group.COLLEGE_REGISTRAR.getId()),false,0,null);
            userDetailService.saveUserDetail(userDetail);

            College collegeEntity = collegeDtoMapper.collegeRegistartionDtoToEntity(collegeRegistrationRequestTo);
            collegeEntity.setId(null);
            collegeEntity.setUser(userDetail);

//            collegeEntity.setState(entityManager.getReference(State.class, collegeRegistrationRequestTo.getStateId()));
            collegeEntity.setStateMedicalCouncil(entityManager.getReference(StateMedicalCouncil.class, collegeRegistrationRequestTo.getCouncilId()));
            collegeEntity.setUniversity(entityManager.getReference(University.class, collegeRegistrationRequestTo.getUniversityId()));

            return collegeRepository.saveAndFlush(collegeEntity);
        } else {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            User collegeUserDetail = userDetailService.findUserDetailByUsername(userName);

            if (!collegeUserDetail.getId().equals(collegeRegistrationRequestTo.getUserId())) {
                throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
            }

            if (!collegeUserDetail.getUsername().equals(collegeRegistrationRequestTo.getEmailId()) && userDetailService.findUserDetailByUsername(collegeRegistrationRequestTo.getEmailId()) != null) {
                throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
            }

            College collegeEntity = findByUserDetail(collegeUserDetail.getId());
            if (!collegeEntity.getId().equals(collegeRegistrationRequestTo.getId())) {
                throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
            }

            collegeUserDetail.setUsername(collegeRegistrationRequestTo.getEmailId());
            userDetailService.saveUserDetail(collegeUserDetail);

            Timestamp createdAt = collegeEntity.getCreatedAt();
            collegeEntity = collegeDtoMapper.collegeRegistartionDtoToEntity(collegeRegistrationRequestTo);
            collegeEntity.setCreatedAt(createdAt);
            collegeEntity.setUser(collegeUserDetail);
//            collegeEntity.setState(entityManager.getReference(State.class, collegeRegistrationRequestTo.getStateId()));
            collegeEntity.setStateMedicalCouncil(entityManager.getReference(StateMedicalCouncil.class, collegeRegistrationRequestTo.getCouncilId()));
            collegeEntity.setUniversity(entityManager.getReference(University.class, collegeRegistrationRequestTo.getUniversityId()));

            return collegeRepository.saveAndFlush(collegeEntity);
        }
    }

    @Override
    public College findById(BigInteger collegeId) {
        return collegeRepository.findById(collegeId).orElse(new College());
    }

    @Override
    public College findByUserDetail(BigInteger userDetailId) {
        return collegeRepository.findByUserDetail(userDetailId);
    }
}
