package in.gov.abdm.nmr.db.sql.domain.college_registrar;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import in.gov.abdm.nmr.db.sql.domain.user_detail.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;
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
public class CollegeRegistrarDaoService implements ICollegeRegistrarDaoService {

    private ICollegeRegistrarRepository collegeRegistrarRepository;

    private ICollegeRepository collegeRepository;

    private ICollegeRegistrarMapper collegeRegistrarMapper;

    private EntityManager entityManager;

    private IUserDetailService userDetailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CollegeRegistrarDaoService(ICollegeRegistrarRepository collegeRegistrarRepository, ICollegeRepository collegeRepository, ICollegeRegistrarMapper collegeRegistrarMapper, EntityManager entityManager, IUserDetailService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.collegeRegistrarRepository = collegeRegistrarRepository;
        this.collegeRepository = collegeRepository;
        this.collegeRegistrarMapper = collegeRegistrarMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public CollegeRegistrar saveCollegeRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) {
        User user = new User(collegeRegistrarCreationRequestTo.getUserId(), collegeRegistrarCreationRequestTo.getEmailId(), //
                bCryptPasswordEncoder.encode(collegeRegistrarCreationRequestTo.getPassword()), null, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()));
        userDetailService.saveUserDetail(user);

        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarMapper.collegeRegistrarDtoToEntity(collegeRegistrarCreationRequestTo);
        collegeRegistrarEntity.setUser(user);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetailSearchTO userDetailSearchTO = new UserDetailSearchTO();
        userDetailSearchTO.setUsername(userName);
        College college = collegeRepository.findByUserDetail(userDetailService.searchUserDetail(userDetailSearchTO).getId());

        collegeRegistrarEntity.setCollege(college);

        return collegeRegistrarRepository.saveAndFlush(collegeRegistrarEntity);
    }

    public CollegeRegistrar findCollegeRegistrarById(BigInteger id) {
        return collegeRegistrarRepository.findById(id).orElse(new CollegeRegistrar());
    }
}
