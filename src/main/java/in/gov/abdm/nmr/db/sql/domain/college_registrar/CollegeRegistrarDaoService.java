package in.gov.abdm.nmr.db.sql.domain.college_registrar;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.db.sql.domain.college.College;
import in.gov.abdm.nmr.db.sql.domain.college.ICollegeRepository;
import in.gov.abdm.nmr.db.sql.domain.user.IUserDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.User;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserSearchTO;
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

    private IUserDaoService userDetailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CollegeRegistrarDaoService(ICollegeRegistrarRepository collegeRegistrarRepository, ICollegeRepository collegeRepository, ICollegeRegistrarMapper collegeRegistrarMapper, EntityManager entityManager, IUserDaoService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.collegeRegistrarRepository = collegeRegistrarRepository;
        this.collegeRepository = collegeRepository;
        this.collegeRegistrarMapper = collegeRegistrarMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public CollegeRegistrar saveCollegeRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) {
        User userDetail = new User(collegeRegistrarCreationRequestTo.getUserId(), collegeRegistrarCreationRequestTo.getEmailId(), //
                bCryptPasswordEncoder.encode(collegeRegistrarCreationRequestTo.getPassword()), null, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()));
        userDetailService.saveUserDetail(userDetail);

        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarMapper.collegeRegistrarDtoToEntity(collegeRegistrarCreationRequestTo);
        collegeRegistrarEntity.setUser(userDetail);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchTO userDetailSearchTO = new UserSearchTO();
        userDetailSearchTO.setUsername(userName);
        College college = collegeRepository.findByUserDetail(userDetailService.searchUserDetail(userDetailSearchTO).getId());

//        collegeRegistrarEntity.setCollege(college);

        return collegeRegistrarRepository.saveAndFlush(collegeRegistrarEntity);
    }

    @Override
    public CollegeRegistrar findCollegeRegistrarById(BigInteger id) {
        return collegeRegistrarRepository.findById(id).orElse(new CollegeRegistrar());
    }
    
    @Override
    public CollegeRegistrar findByUserDetail(BigInteger userDetailId) {
        return collegeRegistrarRepository.findByUserDetail(userDetailId);
    }
}
