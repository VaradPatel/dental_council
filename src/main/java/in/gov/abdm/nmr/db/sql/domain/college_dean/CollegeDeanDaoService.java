package in.gov.abdm.nmr.db.sql.domain.college_dean;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import in.gov.abdm.nmr.db.sql.domain.user_detail.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.db.sql.domain.college.College;
import in.gov.abdm.nmr.db.sql.domain.college.ICollegeRepository;
import in.gov.abdm.nmr.db.sql.domain.user_detail.IUserDetailService;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UserDetailSearchTO;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubType;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubTypeEnum;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserType;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserTypeEnum;

@Service
@Transactional
public class CollegeDeanDaoService implements ICollegeDeanDaoService {

    private ICollegeDeanRepository collegeDeanRepository;

    private ICollegeRepository collegeRepository;

    private ICollegeDeanMapper collegeDeanMapper;

    private EntityManager entityManager;

    private IUserDetailService userDetailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CollegeDeanDaoService(ICollegeDeanRepository collegeDeanRepository, ICollegeRepository collegeRepository, ICollegeDeanMapper collegeDeanMapper, EntityManager entityManager, IUserDetailService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.collegeDeanRepository = collegeDeanRepository;
        this.collegeRepository = collegeRepository;
        this.collegeDeanMapper = collegeDeanMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public CollegeDean saveCollegeDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) {
        User user = new User(collegeDeanCreationRequestTo.getUserId(), collegeDeanCreationRequestTo.getEmailId(), //
                bCryptPasswordEncoder.encode(collegeDeanCreationRequestTo.getPassword()), null, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()));
        userDetailService.saveUserDetail(user);

        CollegeDean collegeDeanEntity = collegeDeanMapper.collegeDeanDtoToEntity(collegeDeanCreationRequestTo);
        collegeDeanEntity.setUser(user);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetailSearchTO userDetailSearchTO = new UserDetailSearchTO();
        userDetailSearchTO.setUsername(userName);
        College college = collegeRepository.findByUserDetail(userDetailService.searchUserDetail(userDetailSearchTO).getId());

        collegeDeanEntity.setCollege(college);

        return collegeDeanRepository.saveAndFlush(collegeDeanEntity);
    }

    public CollegeDean findCollegeDeanById(BigInteger id) {
        return collegeDeanRepository.findById(id).orElse(new CollegeDean());
    }
}
