package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.ICollegeDeanMapper;
import in.gov.abdm.nmr.repository.CollegeMasterRepository;
import in.gov.abdm.nmr.repository.ICollegeDeanRepository;
import in.gov.abdm.nmr.repository.ICollegeRepository;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeDeanDaoService;
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
public class CollegeDeanDaoServiceImpl implements ICollegeDeanDaoService {

    private ICollegeDeanRepository collegeDeanRepository;

    private ICollegeRepository collegeRepository;

    private ICollegeDeanMapper collegeDeanMapper;

    private EntityManager entityManager;

    private IUserDaoService userDetailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private IAccessControlService accessControlService;
    
    @Autowired
    private CollegeMasterRepository collegeMasterRepository;
    
    private IPasswordDaoService passwordDaoService;

    public CollegeDeanDaoServiceImpl(ICollegeDeanRepository collegeDeanRepository, ICollegeRepository collegeRepository, ICollegeDeanMapper collegeDeanMapper, //
                                     EntityManager entityManager, IUserDaoService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder, //
                                     IAccessControlService accessControlService, IPasswordDaoService passwordDaoService) {
        this.collegeDeanRepository = collegeDeanRepository;
        this.collegeRepository = collegeRepository;
        this.collegeDeanMapper = collegeDeanMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessControlService = accessControlService;
        this.passwordDaoService = passwordDaoService;
    }

    public CollegeDean saveCollegeDean(BigInteger collegeId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        if (userDetailService.findByUsername(collegeDeanCreationRequestTo.getEmailId()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if (userDetailService.findByUsername(collegeDeanCreationRequestTo.getPhoneNumber()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        CollegeDean collegeDeanEntity = collegeDeanMapper.collegeDeanDtoToEntity(collegeDeanCreationRequestTo);

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
        collegeDeanEntity.setCollege(collegeMaster);

        String hashedPassword = bCryptPasswordEncoder.encode(collegeDeanCreationRequestTo.getPassword());
        User user = new User(collegeDeanCreationRequestTo.getUserId(), collegeDeanCreationRequestTo.getEmailId(), collegeDeanCreationRequestTo.getPhoneNumber(), null, null,//
                hashedPassword, null, true, true, entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), //
                entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE_DEAN.getCode()), entityManager.getReference(UserGroup.class, in.gov.abdm.nmr.enums.Group.COLLEGE_DEAN.getId()), //
                true, 0, null);
        user = userDetailService.save(user);
        
        Password password = new Password(null, hashedPassword, user);
        passwordDaoService.save(password);
        
        collegeDeanEntity.setUser(user);
        return collegeDeanRepository.saveAndFlush(collegeDeanEntity);
    }


    @Override
    public CollegeDean updateCollegeDean(BigInteger collegeId, BigInteger deanId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        User collegeDeanUserDetail;
        CollegeDean collegeDeanEntityOld;
        if (deanId != null || collegeDeanCreationRequestTo.getUserId() != null) {
            collegeDeanCreationRequestTo.setId(deanId);
            collegeDeanUserDetail = userDetailService.findById(collegeDeanCreationRequestTo.getUserId());

            if (!collegeDeanUserDetail.getEmail().equals(collegeDeanCreationRequestTo.getEmailId()) && userDetailService.findByUsername(collegeDeanCreationRequestTo.getEmailId()) != null) {
                throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }

            if (!collegeDeanUserDetail.getMobileNumber().equals(collegeDeanCreationRequestTo.getPhoneNumber()) && userDetailService.findByUsername(collegeDeanCreationRequestTo.getPhoneNumber()) != null) {
                throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }

            collegeDeanEntityOld = findByUserId(collegeDeanUserDetail.getId());
            if (!collegeDeanEntityOld.getId().equals(deanId)) {
                throw new NmrException(FORBIDDEN, HttpStatus.FORBIDDEN);
            }

            collegeDeanUserDetail.setEmail(collegeDeanCreationRequestTo.getEmailId());
            collegeDeanUserDetail.setMobileNumber(collegeDeanCreationRequestTo.getPhoneNumber());
            userDetailService.save(collegeDeanUserDetail);

            CollegeDean collegeDeanEntity = collegeDeanMapper.collegeDeanDtoToEntity(collegeDeanCreationRequestTo);
            collegeDeanEntity.setUser(collegeDeanUserDetail);
            collegeDeanEntity.setCreatedAt(collegeDeanEntityOld.getCreatedAt());
            collegeDeanEntity.setCollege(collegeDeanEntityOld.getCollege());
            return collegeDeanRepository.saveAndFlush(collegeDeanEntity);

        } else if (userDetailService.findByUsername(collegeDeanCreationRequestTo.getEmailId()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        } else if (userDetailService.findByUsername(collegeDeanCreationRequestTo.getPhoneNumber()) != null) {
            throw new NmrException(USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    public CollegeDean findCollegeDeanById(BigInteger collegeId, BigInteger deanId) throws NmrException {
        CollegeDean collegeDeanEntity = collegeDeanRepository.findCollegeDeanById(collegeId, deanId);
        if (collegeDeanEntity == null) {
            throw new NmrException(INVALID_COLLEGE_ID, HttpStatus.BAD_REQUEST);
        }
        accessControlService.validateUser(collegeDeanEntity.getUser().getId());
        return collegeDeanEntity;
    }

    @Override
    public CollegeDean findByUserId(BigInteger userId) {
        return collegeDeanRepository.findByUserId(userId);
    }

}
