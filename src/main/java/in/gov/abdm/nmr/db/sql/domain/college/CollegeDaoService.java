package in.gov.abdm.nmr.db.sql.domain.college;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import in.gov.abdm.nmr.db.sql.domain.user_detail.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.db.sql.domain.state.State;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncil;
import in.gov.abdm.nmr.db.sql.domain.university.University;
import in.gov.abdm.nmr.db.sql.domain.user_detail.IUserDetailService;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubType;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubTypeEnum;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserType;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserTypeEnum;

@Service
@Transactional
public class CollegeDaoService implements ICollegeDaoService {

    private ICollegeRepository collegeRepository;

    private ICollegeDtoMapper collegeDtoMapper;

    private EntityManager entityManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private IUserDetailService userDetailService;

    public CollegeDaoService(ICollegeRepository collegeRepository, ICollegeDtoMapper collegeDtoMapper, EntityManager entityManager, BCryptPasswordEncoder bCryptPasswordEncoder, IUserDetailService userDetailService) {
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

    public College saveCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo) {
        User user = new User(collegeRegistrationRequestTo.getUserId(), collegeRegistrationRequestTo.getEmailId(), bCryptPasswordEncoder.encode("123456"), null, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()));
        userDetailService.saveUserDetail(user);

        College collegeEntity = collegeDtoMapper.collegeRegistartionDtoToEntity(collegeRegistrationRequestTo);
        collegeEntity.setUser(user);

        collegeEntity.setState(entityManager.getReference(State.class, collegeRegistrationRequestTo.getStateId()));
        collegeEntity.setStateMedicalCouncil(entityManager.getReference(StateMedicalCouncil.class, collegeRegistrationRequestTo.getCouncilId()));
        collegeEntity.setUniversity(entityManager.getReference(University.class, collegeRegistrationRequestTo.getUniversityId()));

        return collegeRepository.saveAndFlush(collegeEntity);
    }

    public College findById(BigInteger collegeId) {
        return collegeRepository.findById(collegeId).orElse(new College());
    }
}
