package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserGroup;
import in.gov.abdm.nmr.entity.UserSubType;
import in.gov.abdm.nmr.entity.UserType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.ICollegeDeanMapper;
import in.gov.abdm.nmr.repository.ICollegeDeanRepository;
import in.gov.abdm.nmr.repository.ICollegeRepository;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeDeanDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;

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

    public CollegeDeanDaoServiceImpl(ICollegeDeanRepository collegeDeanRepository, ICollegeRepository collegeRepository, ICollegeDeanMapper collegeDeanMapper, //
                                     EntityManager entityManager, IUserDaoService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder, //
                                     IAccessControlService accessControlService) {
        this.collegeDeanRepository = collegeDeanRepository;
        this.collegeRepository = collegeRepository;
        this.collegeDeanMapper = collegeDeanMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessControlService = accessControlService;
    }

    public CollegeDean saveCollegeDean(BigInteger collegeId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        User collegeDeanUserDetail = null;
        CollegeDean collegeDeanEntityOld = null;
        if (collegeDeanCreationRequestTo.getId() != null || collegeDeanCreationRequestTo.getUserId() != null) {
            accessControlService.validateUser(collegeDeanCreationRequestTo.getUserId());
            collegeDeanUserDetail = userDetailService.findById(collegeDeanCreationRequestTo.getUserId());

            if (!collegeDeanUserDetail.getUsername().equals(collegeDeanCreationRequestTo.getEmailId()) && userDetailService.findByUsername(collegeDeanCreationRequestTo.getEmailId()) != null) {
                throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
            }

            collegeDeanEntityOld = findByUserDetail(collegeDeanUserDetail.getId());
            if (!collegeDeanEntityOld.getId().equals(collegeDeanCreationRequestTo.getId())) {
                throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
            }

            collegeDeanUserDetail.setUsername(collegeDeanCreationRequestTo.getEmailId());
            if (collegeDeanCreationRequestTo.getPassword() != null) {
                collegeDeanUserDetail.setPassword(bCryptPasswordEncoder.encode(collegeDeanCreationRequestTo.getPassword()));
            }
            userDetailService.saveUserDetail(collegeDeanUserDetail);

            CollegeDean collegeDeanEntity = collegeDeanMapper.collegeDeanDtoToEntity(collegeDeanCreationRequestTo);
            collegeDeanEntity.setUser(collegeDeanUserDetail);
            collegeDeanEntity.setCreatedAt(collegeDeanEntityOld.getCreatedAt());
            collegeDeanEntity.setCollege(collegeDeanEntityOld.getCollege());
            return collegeDeanRepository.saveAndFlush(collegeDeanEntity);

        } else if (userDetailService.findByUsername(collegeDeanCreationRequestTo.getEmailId()) != null) {
            throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
        }


        User userDetail = new User(collegeDeanCreationRequestTo.getUserId(), collegeDeanCreationRequestTo.getEmailId(), //
                bCryptPasswordEncoder.encode(collegeDeanCreationRequestTo.getPassword()), null, true, true, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE_DEAN.getCode()), entityManager.getReference(UserGroup.class, in.gov.abdm.nmr.enums.Group.COLLEGE_DEAN.getId()), true, 0, null);
        userDetailService.saveUserDetail(userDetail);

        CollegeDean collegeDeanEntity = collegeDeanMapper.collegeDeanDtoToEntity(collegeDeanCreationRequestTo);
        collegeDeanEntity.setUser(userDetail);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userDetailService.findByUsername(userName);
        if(!Group.COLLEGE_ADMIN.getId().equals(loggedInUser.getGroup().getId())) {
            throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
        }
        
        College loggedIncollege = collegeRepository.findByUserDetail(loggedInUser.getId());
        College inputCollege = collegeRepository.findById(collegeId).orElse(new College());
        if(!loggedIncollege.getId().equals(inputCollege.getId())) {
            throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
        }
        
        collegeDeanEntity.setCollege(loggedIncollege);
        return collegeDeanRepository.saveAndFlush(collegeDeanEntity);
    }

    public CollegeDean findCollegeDeanById(BigInteger id) throws NmrException {
        CollegeDean collegeDeanEntity = collegeDeanRepository.findById(id).orElse(null);
        if (collegeDeanEntity == null) {
            throw new NmrException("Invalid college id", HttpStatus.BAD_REQUEST);
        }
        accessControlService.validateUser(collegeDeanEntity.getUser().getId());
        return collegeDeanEntity;
    }

    @Override
    public CollegeDean findByUserDetail(BigInteger userDetailId) {
        return collegeDeanRepository.findByUserDetail(userDetailId);
    }

}
