package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.ICollegeRegistrarMapper;
import in.gov.abdm.nmr.repository.CollegeMasterRepository;
import in.gov.abdm.nmr.repository.ICollegeRegistrarRepository;
import in.gov.abdm.nmr.repository.ICollegeRepository;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeRegistrarDaoService;
import in.gov.abdm.nmr.service.IPasswordDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
@Transactional
public class CollegeRegistrarDaoServiceImpl implements ICollegeRegistrarDaoService {

    private ICollegeRegistrarRepository collegeRegistrarRepository;

    private ICollegeRepository collegeRepository;
    @Autowired
    private CollegeMasterRepository collegeMasterRepository;

    private ICollegeRegistrarMapper collegeRegistrarMapper;

    private EntityManager entityManager;

    private IUserDaoService userDetailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private IAccessControlService accessControlService;
    
    private IPasswordDaoService passwordDaoService;

    public CollegeRegistrarDaoServiceImpl(ICollegeRegistrarRepository collegeRegistrarRepository, ICollegeRepository collegeRepository, //
                                          ICollegeRegistrarMapper collegeRegistrarMapper, EntityManager entityManager, IUserDaoService userDetailService, //
                                          BCryptPasswordEncoder bCryptPasswordEncoder, IAccessControlService accessControlService, IPasswordDaoService passwordDaoService) {
        this.collegeRegistrarRepository = collegeRegistrarRepository;
        this.collegeRepository = collegeRepository;
        this.collegeRegistrarMapper = collegeRegistrarMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessControlService = accessControlService;
        this.passwordDaoService = passwordDaoService;
    }

    @Override
    public CollegeRegistrar saveCollegeRegistrar(BigInteger collegeId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        if (userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getEmailId()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if (userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getPhoneNumber()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarMapper.collegeRegistrarDtoToEntity(collegeRegistrarCreationRequestTo);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userDetailService.findByUsername(userName);
        if (!Group.COLLEGE_ADMIN.getId().equals(loggedInUser.getGroup().getId())) {
            throw new NmrException(FORBIDDEN, HttpStatus.FORBIDDEN);
        }
        College loggedIncollege = collegeRepository.findByUserId(loggedInUser.getId());
        College inputCollege = collegeRepository.findById(collegeId).orElse(new College());
        if (!loggedIncollege.getId().equals(inputCollege.getId())) {
            throw new NmrException(FORBIDDEN, HttpStatus.FORBIDDEN);
        }
        CollegeMaster collegeMaster = collegeMasterRepository.findCollegeMasterById(loggedIncollege.getCollegeMaster().getId());
        collegeRegistrarEntity.setCollege(collegeMaster);

        String hashedPassword = bCryptPasswordEncoder.encode(collegeRegistrarCreationRequestTo.getPassword());
        User user = new User(collegeRegistrarCreationRequestTo.getUserId(), collegeRegistrarCreationRequestTo.getEmailId(), collegeRegistrarCreationRequestTo.getPhoneNumber(), //
                null, null, hashedPassword, null, true, true, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE_REGISTRAR.getCode()), entityManager.getReference(UserGroup.class, in.gov.abdm.nmr.enums.Group.COLLEGE_REGISTRAR.getId()), true, 0, null);
        user = userDetailService.save(user);
        
        Password password = new Password(null, hashedPassword, user);
        passwordDaoService.save(password);

        collegeRegistrarEntity.setUser(user);
        return collegeRegistrarRepository.saveAndFlush(collegeRegistrarEntity);
    }


    @Override
    public CollegeRegistrar updateRegisterRegistrar(BigInteger collegeId, BigInteger registrarId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        User collegeRegistrarUserDetail;
        CollegeRegistrar collegeRegistrarEntityOld;
        if (registrarId != null || collegeRegistrarCreationRequestTo.getUserId() != null) {
            collegeRegistrarCreationRequestTo.setId(registrarId);
            accessControlService.validateUser(collegeRegistrarCreationRequestTo.getUserId());
            collegeRegistrarUserDetail = userDetailService.findById(collegeRegistrarCreationRequestTo.getUserId());
            if (!collegeRegistrarUserDetail.getEmail().equals(collegeRegistrarCreationRequestTo.getEmailId()) && userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getEmailId()) != null) {
                throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }

            if (!collegeRegistrarUserDetail.getMobileNumber().equals(collegeRegistrarCreationRequestTo.getPhoneNumber()) && userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getPhoneNumber()) != null) {
                throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
            collegeRegistrarEntityOld = findByUserId(collegeRegistrarUserDetail.getId());
            if (!collegeRegistrarEntityOld.getId().equals(registrarId)) {
                throw new NmrException(FORBIDDEN, HttpStatus.FORBIDDEN);
            }
            collegeRegistrarUserDetail.setEmail(collegeRegistrarCreationRequestTo.getEmailId());
            collegeRegistrarUserDetail.setMobileNumber(collegeRegistrarCreationRequestTo.getPhoneNumber());
            userDetailService.save(collegeRegistrarUserDetail);

            CollegeRegistrar collegeRegistrarEntity = collegeRegistrarMapper.collegeRegistrarDtoToEntity(collegeRegistrarCreationRequestTo);
            collegeRegistrarEntity.setUser(collegeRegistrarUserDetail);
            collegeRegistrarEntity.setCreatedAt(collegeRegistrarEntityOld.getCreatedAt());
            collegeRegistrarEntity.setCollege(collegeRegistrarEntityOld.getCollege());
            return collegeRegistrarRepository.saveAndFlush(collegeRegistrarEntity);

        } else if (userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getEmailId()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        } else if (userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getPhoneNumber()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public CollegeRegistrar findCollegeRegistrarById(BigInteger registrarId, BigInteger collegeId) throws NmrException {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarRepository.getCollegeRegistrarByIds(registrarId, collegeId);
        if (collegeRegistrarEntity == null) {
            throw new NmrException(INVALID_COLLEGE_ID, HttpStatus.BAD_REQUEST);
        }
        accessControlService.validateUser(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarEntity;
    }

    @Override
    public CollegeRegistrar findByUserId(BigInteger userId) {
        return collegeRegistrarRepository.findByUserId(userId);
    }
}
