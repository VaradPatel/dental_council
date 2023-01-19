package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import in.gov.abdm.nmr.mapper.ICollegeRegistrarMapper;
import in.gov.abdm.nmr.repository.ICollegeRegistrarRepository;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.ICollegeRegistrarDaoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.repository.ICollegeRepository;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;

@Service
@Transactional
public class CollegeRegistrarDaoServiceImpl implements ICollegeRegistrarDaoService {

    private ICollegeRegistrarRepository collegeRegistrarRepository;

    private ICollegeRepository collegeRepository;

    private ICollegeRegistrarMapper collegeRegistrarMapper;

    private EntityManager entityManager;

    private IUserDaoService userDetailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    private IAccessControlService accessControlService;

    public CollegeRegistrarDaoServiceImpl(ICollegeRegistrarRepository collegeRegistrarRepository, ICollegeRepository collegeRepository, //
                                          ICollegeRegistrarMapper collegeRegistrarMapper, EntityManager entityManager, IUserDaoService userDetailService, //
                                          BCryptPasswordEncoder bCryptPasswordEncoder, IAccessControlService accessControlService) {
        this.collegeRegistrarRepository = collegeRegistrarRepository;
        this.collegeRepository = collegeRepository;
        this.collegeRegistrarMapper = collegeRegistrarMapper;
        this.entityManager = entityManager;
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessControlService = accessControlService;
    }

    @Override
    public CollegeRegistrar saveCollegeRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException {
        User collegeRegistrarUserDetail = null;
        CollegeRegistrar collegeRegistrarEntityOld = null;
        if (collegeRegistrarCreationRequestTo.getId() != null || collegeRegistrarCreationRequestTo.getUserId() != null) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            collegeRegistrarUserDetail = userDetailService.findByUsername(userName);

            if (!collegeRegistrarUserDetail.getId().equals(collegeRegistrarCreationRequestTo.getUserId())) {
                throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
            }

            if (!collegeRegistrarUserDetail.getUsername().equals(collegeRegistrarCreationRequestTo.getEmailId()) && userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getEmailId()) != null) {
                throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
            }

            collegeRegistrarEntityOld = findByUserDetail(collegeRegistrarUserDetail.getId());
            if (!collegeRegistrarEntityOld.getId().equals(collegeRegistrarCreationRequestTo.getId())) {
                throw new NmrException("Forbidden", HttpStatus.FORBIDDEN);
            }
        } else if (userDetailService.findByUsername(collegeRegistrarCreationRequestTo.getEmailId()) != null) {
            throw new NmrException("User already exists", HttpStatus.BAD_REQUEST);
        }

        User userDetail = new User(collegeRegistrarCreationRequestTo.getUserId(), collegeRegistrarCreationRequestTo.getEmailId(), //
                bCryptPasswordEncoder.encode(collegeRegistrarCreationRequestTo.getPassword()), null, true, true, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE_REGISTRAR.getCode()) , entityManager.getReference(Group.class, in.gov.abdm.nmr.enums.Group.COLLEGE_REGISTRAR.getId()),false,0,null);

        if (collegeRegistrarUserDetail != null) {
            userDetail.setCreatedAt(collegeRegistrarUserDetail.getCreatedAt());
            userDetail.setRefreshTokenId(collegeRegistrarUserDetail.getRefreshTokenId());
        }
        userDetailService.saveUserDetail(userDetail);

        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarMapper.collegeRegistrarDtoToEntity(collegeRegistrarCreationRequestTo);
        collegeRegistrarEntity.setUser(userDetail);

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchTO userDetailSearchTO = new UserSearchTO();
        userDetailSearchTO.setUsername(userName);

        College college = collegeRepository.findByUserDetail(userDetailService.searchUserDetail(userDetailSearchTO).getId());
        collegeRegistrarEntity.setCollege(college);

        if (collegeRegistrarEntityOld != null) {
            collegeRegistrarEntity.setCreatedAt(collegeRegistrarEntityOld.getCreatedAt());
        }
        return collegeRegistrarRepository.saveAndFlush(collegeRegistrarEntity);
    }

    @Override
    public CollegeRegistrar findCollegeRegistrarById(BigInteger id) throws NmrException {
        CollegeRegistrar collegeRegistrarEntity = collegeRegistrarRepository.findById(id).orElse(null);
        if (collegeRegistrarEntity == null) {
            throw new NmrException("Invalid college id", HttpStatus.BAD_REQUEST);
        }
        accessControlService.validateUser(collegeRegistrarEntity.getUser().getId());
        return collegeRegistrarEntity;
    }

    @Override
    public CollegeRegistrar findByUserDetail(BigInteger userDetailId) {
        return collegeRegistrarRepository.findByUserDetail(userDetailId);
    }
}
