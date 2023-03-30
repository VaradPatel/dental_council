package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.CollegeMasterDataTO;
import in.gov.abdm.nmr.dto.CollegeMasterTOV2;
import in.gov.abdm.nmr.dto.CollegeProfileTOV2;
import in.gov.abdm.nmr.dto.SendLinkOnMailTo;
import in.gov.abdm.nmr.dto.UniversityMasterTo;
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
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.service.ICollegeMasterDaoService;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;
import in.gov.abdm.nmr.service.ICollegeServiceV2;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.service.IStateMedicalCouncilDaoService;
import in.gov.abdm.nmr.service.IUniversityMasterService;
import in.gov.abdm.nmr.service.IUserDaoService;

@Service
@Transactional
public class CollegeServiceV2Impl implements ICollegeServiceV2 {

    private static final String UNSUPPORTED_OPERATION = "Unsupported operation";

    private static final String ACCESS_DENIED = "Access denied";

    private ICollegeMasterDaoService collegeMasterDaoService;

    private IUniversityMasterService universityMasterService;

    private IStateMedicalCouncilDaoService stateMedicalCouncilDaoService;

    private ICollegeProfileDaoService collegeProfileDaoService;

    private IUserDaoService userDaoService;

    private EntityManager entityManager;

    @Autowired
    private IPasswordService passwordService;

    public CollegeServiceV2Impl(ICollegeMasterDaoService collegeMasterDaoService, IUniversityMasterService universityMasterService, //
                                IStateMedicalCouncilDaoService stateMedicalCouncilDaoService, ICollegeProfileDaoService collegeProfileDaoService, //
                                IUserDaoService userDaoService, EntityManager entityManager) {
        this.collegeMasterDaoService = collegeMasterDaoService;
        this.universityMasterService = universityMasterService;
        this.stateMedicalCouncilDaoService = stateMedicalCouncilDaoService;
        this.collegeProfileDaoService = collegeProfileDaoService;
        this.userDaoService = userDaoService;
        this.entityManager = entityManager;
    }

    @Override
    public List<CollegeMasterDataTO> getAllColleges() throws NmrException {
        List<CollegeMasterDataTO> collegeMasterTOList = new ArrayList<>();
        for (CollegeMaster collegeMaster : collegeMasterDaoService.getAllColleges()) {
            CollegeMasterDataTO collegeMasterDataTO = new CollegeMasterDataTO();
            collegeMasterDataTO.setId(collegeMaster.getId());
            collegeMasterDataTO.setName(collegeMaster.getName());
            collegeMasterTOList.add(collegeMasterDataTO);
        }
        return collegeMasterTOList;
    }

    @Override
    public CollegeMasterTOV2 getCollege(BigInteger id) throws NmrException {
        CollegeMaster collegeMaster = preCollegeAccessChecks(id);

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

        CollegeProfile collegeprofile = collegeProfileDaoService.findAdminByCollegeId(id);
        if (collegeprofile != null) {
            BigInteger userId = collegeprofile.getUser().getId();
            User collegeUser = userDaoService.findById(userId);
            collegeMasterTO.setEmailId(collegeUser.getEmail());
            collegeMasterTO.setMobileNumber(collegeUser.getMobileNumber());
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

    private CollegeMaster preCollegeAccessChecks(BigInteger id) throws NmrException {
        User loggedInUser = getLoggedInUser();
        CollegeMaster collegeMaster = collegeMasterDaoService.findById(id);
        if (collegeMaster == null) {
            throw new NmrException("No college found for id", HttpStatus.NOT_FOUND);
        }

        if (UserSubTypeEnum.COLLEGE.getCode().equals(loggedInUser.getUserSubType() != null ? loggedInUser.getUserSubType().getId() : null)) {
            CollegeProfile collegeprofile = collegeProfileDaoService.findAdminByCollegeId(collegeMaster.getId());

            if (collegeprofile == null) {
                throw new NmrException(UNSUPPORTED_OPERATION, HttpStatus.BAD_REQUEST);
            }

            if (!loggedInUser.getId().equals(collegeprofile.getUser().getId())) {
                throw new NmrException(ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        }
        return collegeMaster;
    }

    @Override
    public CollegeMasterTOV2 createOrUpdateCollege(CollegeMasterTOV2 collegeMasterTOV2) throws NmrException {
        CollegeMaster collegeMaster = null;
        CollegeProfile collegeProfile = null;

        if (collegeMasterTOV2.getId() != null) {
            collegeMaster = collegeMasterDaoService.findById(collegeMasterTOV2.getId());

            if (collegeMaster == null) {
                throw new NmrException("No college found for id", HttpStatus.NOT_FOUND);
            }

            collegeProfile = collegeProfileDaoService.findAdminByCollegeId(collegeMaster.getId());
            preCollegeUpdationChecks(collegeMasterTOV2, collegeMaster, collegeProfile);
        } else {
            duplicateContactsCheck(collegeMasterTOV2.getEmailId(), collegeMasterTOV2.getMobileNumber());
        }

        collegeMaster = collegeMaster != null ? collegeMaster : new CollegeMaster();
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

        User user = collegeProfile != null ? collegeProfile.getUser() : null;
        if (user == null) {
            user = new User(null, collegeMasterTOV2.getEmailId(), collegeMasterTOV2.getMobileNumber(), null, null, null, false, false, //
                    entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()), // 
                    entityManager.getReference(UserGroup.class, Group.COLLEGE.getId()), true, 0, null, null, null, null, false);
        } else {
            user.setEmail(collegeMasterTOV2.getEmailId());
            user.setMobileNumber(collegeMasterTOV2.getMobileNumber());
        }
        user = userDaoService.save(user);

        boolean isNewCollegeProfile = collegeProfile == null;
        if (isNewCollegeProfile) {
            collegeProfile = new CollegeProfile();
        }
        collegeProfile.setName(collegeMaster.getName());
        collegeProfile.setCollege(collegeMaster);
        collegeProfile.setUser(user);
        collegeProfileDaoService.save(collegeProfile);
        
        if(isNewCollegeProfile) {
            passwordService.getResetPasswordLink(new SendLinkOnMailTo(user.getEmail()));
        }
        return collegeMasterTOV2;
    }

    private CollegeProfile preCollegeUpdationChecks(CollegeMasterTOV2 collegeMasterTOV2, CollegeMaster collegeMaster, CollegeProfile collegeProfile) throws NmrException {
        User loggedInUser = getLoggedInUser();

        if (collegeMaster == null) {
            throw new NmrException("No college found for id", HttpStatus.NOT_FOUND);
        }

        if (UserSubTypeEnum.COLLEGE.getCode().equals(loggedInUser.getUserSubType() != null ? loggedInUser.getUserSubType().getId() : null)) {
            if (collegeProfile == null) {
                throw new NmrException(UNSUPPORTED_OPERATION, HttpStatus.BAD_REQUEST);
            }

            User user = collegeProfile.getUser();
            if (!collegeMasterTOV2.getEmailId().equals(user.getEmail()) || !collegeMasterTOV2.getMobileNumber().equals(user.getMobileNumber())) {
                duplicateContactsCheck(collegeMasterTOV2.getEmailId(), collegeMasterTOV2.getMobileNumber());
            }

            if (!loggedInUser.getId().equals(collegeProfile.getUser().getId())) {
                throw new NmrException(ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        }
        return collegeProfile;
    }

    @Override
    public List<CollegeMasterDataTO> getAllCollegeVerifiersDesignation() throws NmrException {
        return entityManager.createQuery("select ust from userSubType ust where ust.id != ?1 and ust.userType.id = ?2", UserSubType.class) //
                .setParameter(1, UserSubTypeEnum.COLLEGE.getCode()).setParameter(2, UserTypeEnum.COLLEGE.getCode()) //
                .getResultList().stream().map(userSubType -> new CollegeMasterDataTO(userSubType.getId(), userSubType.getName())).toList();
    }

    @Override
    public CollegeProfileTOV2 createOrUpdateCollegeVerifier(CollegeProfileTOV2 collegeProfileTOV2) throws GeneralSecurityException, NmrException {
        CollegeProfile collegeProfile = null;

        if (collegeProfileTOV2.getId() != null) {
            collegeProfile = collegeProfileDaoService.findById(collegeProfileTOV2.getId());
            preVerifierUpdationChecks(collegeProfileTOV2, collegeProfile);
        } else {
            duplicateContactsCheck(collegeProfileTOV2.getEmailId(), collegeProfileTOV2.getMobileNumber());
        }

        User user = collegeProfile != null ? collegeProfile.getUser() : null;
        if (user == null) {
            user = new User(null, collegeProfileTOV2.getEmailId(), collegeProfileTOV2.getMobileNumber(), null, //
                    null, null, false, false, entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getCode()), //
                    entityManager.getReference(UserSubType.class, collegeProfileTOV2.getDesignation()), //
                    entityManager.getReference(UserGroup.class, Group.COLLEGE.getId()), true, 0, null, null, null, null, false);
        } else {
            user.setEmail(collegeProfileTOV2.getEmailId());
            user.setMobileNumber(collegeProfileTOV2.getMobileNumber());
        }
        user = userDaoService.save(user);

        boolean isNewCollegeVerifierProfile = collegeProfile == null;
        if (isNewCollegeVerifierProfile) {
            collegeProfile = new CollegeProfile();
        }
        collegeProfile.setName(collegeProfileTOV2.getName());
        collegeProfile.setCollege(entityManager.getReference(CollegeMaster.class, collegeProfileTOV2.getCollegeId()));
        collegeProfile.setUser(user);
        collegeProfile = collegeProfileDaoService.save(collegeProfile);

        collegeProfileTOV2.setId(collegeProfile.getId());
        
        if (isNewCollegeVerifierProfile) {
            passwordService.getResetPasswordLink(new SendLinkOnMailTo(user.getEmail()));
        }
        return collegeProfileTOV2;
    }

    private void duplicateContactsCheck(String emailId, String mobileNumber) throws NmrException {
        if (userDaoService.existsByEmail(emailId)) {
            throw new NmrException("Email id already registered", HttpStatus.NOT_FOUND);
        }

        if (userDaoService.existsByMobileNumber(mobileNumber)) {
            throw new NmrException("Mobile number already registered", HttpStatus.NOT_FOUND);
        }
    }

    private void preVerifierUpdationChecks(CollegeProfileTOV2 collegeProfileTOV2, CollegeProfile collegeProfile) throws NmrException {
        User loggedInUser = getLoggedInUser();
        if (collegeProfile == null) {
            throw new NmrException("No college verifier found for id", HttpStatus.BAD_REQUEST);
        }

        User user = collegeProfile.getUser();
        if (!collegeProfileTOV2.getEmailId().equals(user.getEmail()) || !collegeProfileTOV2.getMobileNumber().equals(user.getMobileNumber())) {
            duplicateContactsCheck(collegeProfileTOV2.getEmailId(), collegeProfileTOV2.getMobileNumber());
        }

        if (!loggedInUser.getId().equals(collegeProfile.getUser().getId())) {
            throw new NmrException(ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        if (!collegeProfileTOV2.getCollegeId().equals(collegeProfile.getCollege().getId())) {
            throw new NmrException(ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public CollegeProfileTOV2 getCollegeVerifier(BigInteger collegeId, BigInteger verifierId) throws NmrException {
        CollegeProfile collegeProfile = preVerifierAccessChecks(collegeId, verifierId);

        CollegeProfileTOV2 collegeProfileTOV2 = new CollegeProfileTOV2();
        collegeProfileTOV2.setId(collegeProfile.getId());
        collegeProfileTOV2.setName(collegeProfile.getName());
        collegeProfileTOV2.setCollegeId(collegeProfile.getCollege().getId());

        User user = collegeProfile.getUser();
        collegeProfileTOV2.setEmailId(user.getEmail());
        collegeProfileTOV2.setMobileNumber(user.getMobileNumber());
        collegeProfileTOV2.setDesignation(user.getUserSubType().getId());
        return collegeProfileTOV2;
    }

    private CollegeProfile preVerifierAccessChecks(BigInteger collegeId, BigInteger verifierId) throws NmrException {
        User loggedInUser = getLoggedInUser();
        CollegeProfile collegeProfile = collegeProfileDaoService.findById(verifierId);

        if (collegeProfile == null) {
            throw new NmrException("No college verifier found for id", HttpStatus.BAD_REQUEST);
        }

        if (!loggedInUser.getId().equals(collegeProfile.getUser().getId())) {
            throw new NmrException(ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        if (!collegeId.equals(collegeProfile.getCollege().getId())) {
            throw new NmrException(ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        return collegeProfile;
    }

    private User getLoggedInUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDaoService.findByUsername(userName);
    }
}
