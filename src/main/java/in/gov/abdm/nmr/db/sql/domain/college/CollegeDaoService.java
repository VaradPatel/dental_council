package in.gov.abdm.nmr.db.sql.domain.college;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.db.sql.domain.state.State;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncil;
import in.gov.abdm.nmr.db.sql.domain.university.University;
import in.gov.abdm.nmr.db.sql.domain.user.IUserDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.User;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubType;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubTypeEnum;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserType;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;

@Service
@Transactional
public class CollegeDaoService implements ICollegeDaoService {

    private ICollegeRepository collegeRepository;

    private ICollegeDtoMapper collegeDtoMapper;

    private EntityManager entityManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private IUserDaoService userDetailService;

    public CollegeDaoService(ICollegeRepository collegeRepository, ICollegeDtoMapper collegeDtoMapper, EntityManager entityManager, BCryptPasswordEncoder bCryptPasswordEncoder, IUserDaoService userDetailService) {
        this.collegeRepository = collegeRepository;
        this.collegeDtoMapper = collegeDtoMapper;
        this.entityManager = entityManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

            User userDetail = new User(null, collegeRegistrationRequestTo.getEmailId(), bCryptPasswordEncoder.encode("123456"), null, //
                    entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()));
            userDetailService.saveUserDetail(userDetail);

            College collegeEntity = collegeDtoMapper.collegeRegistartionDtoToEntity(collegeRegistrationRequestTo);
            collegeEntity.setId(null);
            collegeEntity.setUser(userDetail);

            collegeEntity.setState(entityManager.getReference(State.class, collegeRegistrationRequestTo.getStateId()));
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
            collegeEntity.setState(entityManager.getReference(State.class, collegeRegistrationRequestTo.getStateId()));
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
