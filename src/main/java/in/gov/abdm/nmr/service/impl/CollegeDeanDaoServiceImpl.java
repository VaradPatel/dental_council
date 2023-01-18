package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.mapper.ICollegeDeanMapper;
import in.gov.abdm.nmr.repository.ICollegeDeanRepository;
import in.gov.abdm.nmr.service.ICollegeDeanDaoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.repository.ICollegeRepository;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;

@Service
@Transactional
public class CollegeDeanDaoServiceImpl implements ICollegeDeanDaoService {

    private ICollegeDeanRepository collegeDeanRepository;

    private ICollegeRepository collegeRepository;

    private ICollegeDeanMapper collegeDeanMapper;

    private EntityManager entityManager;

    private IUserDaoService userDetailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CollegeDeanDaoServiceImpl(ICollegeDeanRepository collegeDeanRepository, ICollegeRepository collegeRepository, ICollegeDeanMapper collegeDeanMapper, EntityManager entityManager, IUserDaoService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.collegeDeanRepository = collegeDeanRepository;
        this.collegeRepository = collegeRepository;
        this.collegeDeanMapper = collegeDeanMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public CollegeDean saveCollegeDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException {
        User collegeDeanUserDetail = null;
        CollegeDean collegeDeanEntityOld = null;
        if (collegeDeanCreationRequestTo.getId() != null || collegeDeanCreationRequestTo.getUserId() != null) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            collegeDeanUserDetail = userDetailService.findUserDetailByUsername(userName);

            if (!collegeDeanUserDetail.getId().equals(collegeDeanCreationRequestTo.getUserId())) {
                throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
            }

            if (!collegeDeanUserDetail.getUsername().equals(collegeDeanCreationRequestTo.getEmailId()) && userDetailService.findUserDetailByUsername(collegeDeanCreationRequestTo.getEmailId()) != null) {
                throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
            }

            collegeDeanEntityOld = findByUserDetail(collegeDeanUserDetail.getId());
            if (!collegeDeanEntityOld.getId().equals(collegeDeanCreationRequestTo.getId())) {
                throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
            }
        } else if (userDetailService.findUserDetailByUsername(collegeDeanCreationRequestTo.getEmailId()) != null) {
            throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
        }


        User userDetail = new User(collegeDeanCreationRequestTo.getUserId(), collegeDeanCreationRequestTo.getEmailId(), //
                bCryptPasswordEncoder.encode(collegeDeanCreationRequestTo.getPassword()), null, true, true, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE_DEAN.getCode()),  entityManager.getReference(Group.class, in.gov.abdm.nmr.enums.Group.COLLEGE_DEAN.getId()),false,0,null);

        if (collegeDeanUserDetail != null) {
            userDetail.setCreatedAt(collegeDeanUserDetail.getCreatedAt());
            userDetail.setRefreshTokenId(collegeDeanUserDetail.getRefreshTokenId());
        }

        userDetailService.saveUserDetail(userDetail);

        CollegeDean collegeDeanEntity = collegeDeanMapper.collegeDeanDtoToEntity(collegeDeanCreationRequestTo);
        collegeDeanEntity.setUser(userDetail);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchTO userDetailSearchTO = new UserSearchTO();
        userDetailSearchTO.setUsername(userName);
        College college = collegeRepository.findByUserDetail(userDetailService.searchUserDetail(userDetailSearchTO).getId());
        collegeDeanEntity.setCollege(college);

        if (collegeDeanEntityOld != null) {
            collegeDeanEntity.setCreatedAt(collegeDeanEntityOld.getCreatedAt());
        }
        return collegeDeanRepository.saveAndFlush(collegeDeanEntity);
    }

    public CollegeDean findCollegeDeanById(BigInteger id) {
        return collegeDeanRepository.findById(id).orElse(new CollegeDean());
    }

    @Override
    public CollegeDean findByUserDetail(BigInteger userDetailId) {
        return collegeDeanRepository.findByUserDetail(userDetailId);
    }
}
