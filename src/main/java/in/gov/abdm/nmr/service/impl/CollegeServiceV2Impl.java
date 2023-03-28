package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.entity.CollegeMaster;
import in.gov.abdm.nmr.entity.CollegeProfile;
import in.gov.abdm.nmr.entity.Course;
import in.gov.abdm.nmr.entity.District;
import in.gov.abdm.nmr.entity.State;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.UniversityMaster;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserGroup;
import in.gov.abdm.nmr.entity.UserSubType;
import in.gov.abdm.nmr.entity.UserType;
import in.gov.abdm.nmr.entity.Villages;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NoDataFoundException;
import in.gov.abdm.nmr.security.common.RsaUtil;

@Service
@Transactional
public class CollegeServiceV2Impl implements ICollegeServiceV2 {

    private ICollegeMasterDaoService collegeMasterDaoService;

    private IUniversityMasterService universityMasterService;

    private IStateMedicalCouncilDaoService stateMedicalCouncilDaoService;

    private ICollegeProfileDaoService collegeProfileDaoService;

    private IUserDaoService userDaoService;

    private EntityManager entityManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RsaUtil rsaUtil;

    @Autowired
    private IPasswordService passwordService;

    public CollegeServiceV2Impl(ICollegeMasterDaoService collegeMasterDaoService, IUniversityMasterService universityMasterService, //
                                IStateMedicalCouncilDaoService stateMedicalCouncilDaoService, ICollegeProfileDaoService collegeProfileDaoService, //
                                IUserDaoService userDaoService, EntityManager entityManager, BCryptPasswordEncoder bCryptPasswordEncoder, RsaUtil rsaUtil) {
        this.collegeMasterDaoService = collegeMasterDaoService;
        this.universityMasterService = universityMasterService;
        this.stateMedicalCouncilDaoService = stateMedicalCouncilDaoService;
        this.collegeProfileDaoService = collegeProfileDaoService;
        this.userDaoService = userDaoService;
        this.entityManager = entityManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.rsaUtil = rsaUtil;
    }

    @Override
    public List<CollegeMasterTOV2> getAllColleges() {
        List<CollegeMasterTOV2> collegeMasterTOList = new ArrayList<>();

        for (CollegeMaster collegeMaster : collegeMasterDaoService.getAllColleges()) {
            CollegeMasterTOV2 collegeMasterTo = new CollegeMasterTOV2();
            collegeMasterTo.setId(collegeMaster.getId());
            collegeMasterTo.setName(collegeMaster.getName());
            collegeMasterTOList.add(collegeMasterTo);
        }
        return collegeMasterTOList;
    }

    @Override
    public CollegeMasterTOV2 getCollege(BigInteger id) {
        CollegeMaster collegeMaster = collegeMasterDaoService.findById(id);
        if (collegeMaster != null) {
            CollegeMasterTOV2 collegeMasterTO = new CollegeMasterTOV2();

            collegeMasterTO.setId(collegeMaster.getId());
            collegeMasterTO.setCollegeCode(collegeMaster.getCollegeCode());
            collegeMasterTO.setName(collegeMaster.getName());
            collegeMasterTO.setWebsite(collegeMaster.getWebsite());
            collegeMasterTO.setAddressLine1(collegeMaster.getAddressLine1());
            collegeMasterTO.setAddressLine2(collegeMaster.getAddressLine2());
            collegeMasterTO.setPinCode(collegeMaster.getPinCode());
            collegeMasterTO.setCourseId(collegeMaster.getCourse() != null ? collegeMaster.getCourse().getId() : null);
            collegeMasterTO.setStateId(collegeMaster.getState() != null ? collegeMaster.getState().getId() : null);
            collegeMasterTO.setDistrictId(collegeMaster.getDistrict() != null ? collegeMaster.getDistrict().getId() : null);
            collegeMasterTO.setVillageId(collegeMaster.getVillage() != null ? collegeMaster.getVillage().getId() : null);

            CollegeProfile collegeprofile = collegeProfileDaoService.findByCollegeId(id);
            if (collegeprofile != null) {
                BigInteger userId = collegeprofile.getUser().getId();
                User collegeUser = userDaoService.findById(userId);
                collegeMasterTO.setEmailId(collegeUser.getEmail());
                collegeMasterTO.setMobileNumber(collegeUser.getEmail());
            }

            List<UniversityMasterTo> university = universityMasterService.getUniversitiesByCollegeId(id);
            collegeMasterTO.setUniversityId(university != null && !university.isEmpty() ? university.get(0).getId() : null);

            if (collegeMaster.getStateMedicalCouncil() == null && collegeMaster.getState() != null) {
                StateMedicalCouncil stateMedicalCouncil = stateMedicalCouncilDaoService.findbyState(String.valueOf(collegeMaster.getState().getId()));
                collegeMasterTO.setStateMedicalCouncilId(stateMedicalCouncil != null ? stateMedicalCouncil.getId() : null);
            } else {
                collegeMasterTO.setStateMedicalCouncilId(collegeMaster.getStateMedicalCouncil().getId());
            }

            return collegeMasterTO;
        }
        throw new NoDataFoundException("No college found for id");
    }

    @Override
    public CollegeMasterTOV2 createOrUpdateCollege(CollegeMasterTOV2 collegeMasterTOV2) {
        CollegeMaster collegeMaster = new CollegeMaster();
        collegeMaster.setId(collegeMasterTOV2.getId());
        collegeMaster.setCollegeCode(collegeMasterTOV2.getCollegeCode());
        collegeMaster.setName(collegeMasterTOV2.getName());
        collegeMaster.setWebsite(collegeMasterTOV2.getWebsite());
        collegeMaster.setAddressLine1(collegeMasterTOV2.getAddressLine1());
        collegeMaster.setAddressLine2(collegeMasterTOV2.getAddressLine2());
        collegeMaster.setPinCode(collegeMasterTOV2.getPinCode());
        collegeMaster.setStatus(BigInteger.valueOf(1));
        collegeMaster.setVisibleStatus(BigInteger.valueOf(1));
        collegeMaster.setSystemOfMedicineId(BigInteger.valueOf(1));
        collegeMaster.setCourse(collegeMasterTOV2.getCourseId() != null ? entityManager.getReference(Course.class, collegeMasterTOV2.getCourseId()) : null);
        collegeMaster.setState(entityManager.getReference(State.class, collegeMasterTOV2.getStateId()));
        collegeMaster.setDistrict(entityManager.getReference(District.class, collegeMasterTOV2.getDistrictId()));
        collegeMaster.setVillage(collegeMasterTOV2.getVillageId() != null ? entityManager.getReference(Villages.class, collegeMasterTOV2.getVillageId()) : null);
        collegeMaster.setStateMedicalCouncil(entityManager.getReference(StateMedicalCouncil.class, collegeMasterTOV2.getStateMedicalCouncilId()));
        collegeMaster = collegeMasterDaoService.save(collegeMaster);
        collegeMasterTOV2.setId(collegeMaster.getId());

        UniversityMaster university = universityMasterService.findById(collegeMasterTOV2.getUniversityId());
        if (university != null && !university.getCollegeId().equals(collegeMaster.getId())) {
            UniversityMaster universityMaster = new UniversityMaster();
            universityMaster.setCollegeId(collegeMaster.getId());
            universityMaster.setName(university.getName());
            universityMaster.setStatus(university.getStatus());
            universityMaster.setVisibleStatus(university.getVisibleStatus());
            universityMasterService.save(universityMaster);
        }

        CollegeProfile collegeProfile = collegeProfileDaoService.findByCollegeId(collegeMaster.getId());

        BigInteger userId = collegeProfile != null ? collegeProfile.getUser().getId() : null;
        User user = new User(userId, collegeMasterTOV2.getEmailId(), collegeMasterTOV2.getMobileNumber(), null, null, null, false, false, //
                entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()), // 
                entityManager.getReference(UserGroup.class, Group.COLLEGE.getId()), true, 0, null, null, null, null, false);
        user = userDaoService.save(user);

        if (collegeProfile == null) {
            collegeProfile = new CollegeProfile();
        }
        collegeProfile.setName(collegeMaster.getName());
        collegeProfile.setCollege(collegeMaster);
        collegeProfile.setUser(user);
        collegeProfileDaoService.save(collegeProfile);

       passwordService.getResetPasswordLink(new SendLinkOnMailTo(user.getEmail()));

        return collegeMasterTOV2;
    }

    @Override
    public List<CollegeDesignationTO> getAllCollegeVerifiersDesignation() {
        return entityManager.createQuery("select ust from userSubType ust where ust.id != ?1 and ust.userType.id = ?2", UserSubType.class) //
                .setParameter(1, UserSubTypeEnum.COLLEGE.getCode()).setParameter(2, UserTypeEnum.COLLEGE.getCode()) //
                .getResultList().stream().map(userSubType -> new CollegeDesignationTO(userSubType.getId().longValueExact(), userSubType.getName())).toList();
    }

    @Override
    public CollegeProfileTOV2 createOrUpdateCollegeVerifier(CollegeProfileTOV2 collegeProfileTOV2) throws GeneralSecurityException {
        CollegeProfile collegeProfile = null;
        if (collegeProfileTOV2.getId() != null) {
            collegeProfile = collegeProfileDaoService.findById(collegeProfileTOV2.getId());
        }

        BigInteger userId = collegeProfile != null ? collegeProfile.getUser().getId() : null;
        User user = new User(userId, collegeProfileTOV2.getEmailId(), collegeProfileTOV2.getMobileNumber(), null, //
                null, null, false, false, entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), //
                entityManager.getReference(UserSubType.class, collegeProfileTOV2.getDesignation()), //
                entityManager.getReference(UserGroup.class, Group.COLLEGE.getId()), true, 0, null, null, null, null, false);
        user = userDaoService.save(user);

        if (collegeProfile == null) {
            collegeProfile = new CollegeProfile();
        }
        collegeProfile.setName(collegeProfileTOV2.getName());
        collegeProfile.setCollege(entityManager.getReference(CollegeMaster.class, collegeProfileTOV2.getCollegeId()));
        collegeProfile.setUser(user);
        collegeProfile = collegeProfileDaoService.save(collegeProfile);

        collegeProfileTOV2.setId(collegeProfile.getId());
        passwordService.getResetPasswordLink(new SendLinkOnMailTo(user.getEmail()));
        return collegeProfileTOV2;
    }

    @Override
    public CollegeProfileTOV2 getCollegeVerifier(BigInteger collegeId, BigInteger verifierId) {
        CollegeProfile collegeProfile = collegeProfileDaoService.findById(verifierId);
        if (collegeProfile != null) {
            CollegeProfileTOV2 collegeProfileTOV2 = new CollegeProfileTOV2();
            collegeProfileTOV2.setId(collegeProfile.getId());
            collegeProfileTOV2.setName(collegeProfile.getName());
            collegeProfileTOV2.setCollegeId(collegeProfile.getCollege().getId());

            User user = collegeProfile.getUser();
            collegeProfileTOV2.setMobileNumber(user.getEmail());
            collegeProfileTOV2.setMobileNumber(user.getMobileNumber());
            collegeProfileTOV2.setDesignation(user.getUserSubType().getId());
            return collegeProfileTOV2;
        }
        throw new NoDataFoundException("No college verifier found for id");
    }
}
