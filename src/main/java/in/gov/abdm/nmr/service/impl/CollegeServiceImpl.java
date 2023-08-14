package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
import in.gov.abdm.nmr.service.ICollegeMasterDaoService;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;
import in.gov.abdm.nmr.service.ICollegeService;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.service.IStateMedicalCouncilDaoService;
import in.gov.abdm.nmr.service.IUniversityMasterService;
import in.gov.abdm.nmr.service.IUserDaoService;

import static in.gov.abdm.nmr.util.NMRConstants.FORBIDDEN;

@Service
@Transactional
public class CollegeServiceImpl implements ICollegeService {

    private static final List<BigInteger> ALLOWED_SUBTYPE_FOR_COLLEGE_UPDATES = List.of(UserSubTypeEnum.COLLEGE_ADMIN.getId(), UserSubTypeEnum.NMC_ADMIN.getId());
    private static final String UNSUPPORTED_OPERATION = "Unsupported operation";

    private ICollegeMasterDaoService collegeMasterDaoService;

    private IUniversityMasterService universityMasterService;

    private IStateMedicalCouncilDaoService stateMedicalCouncilDaoService;

    private ICollegeProfileDaoService collegeProfileDaoService;

    private IUserDaoService userDaoService;

    private EntityManager entityManager;

    @Autowired
    IPasswordService passwordService;

    public CollegeServiceImpl(ICollegeMasterDaoService collegeMasterDaoService, IUniversityMasterService universityMasterService,
                                IStateMedicalCouncilDaoService stateMedicalCouncilDaoService, ICollegeProfileDaoService collegeProfileDaoService,
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
    public CollegeResponseTo getCollege(BigInteger id) throws NmrException, InvalidIdException, NotFoundException {
        CollegeMaster collegeMaster = preCollegeAccessChecks(id);

        CollegeResponseTo collegeMasterTO = new CollegeResponseTo();
        collegeMasterTO.setId(collegeMaster.getId());
        collegeMasterTO.setCollegeCode(collegeMaster.getCollegeCode());
        collegeMasterTO.setName(collegeMaster.getName());
        collegeMasterTO.setWebsite(collegeMaster.getWebsite());
        collegeMasterTO.setAddressLine1(collegeMaster.getAddressLine1());
        collegeMasterTO.setAddressLine2(collegeMaster.getAddressLine2());
        collegeMasterTO.setPinCode(collegeMaster.getPinCode());
        collegeMasterTO.setCourseTO(collegeMaster.getCourse() != null ? CourseTO.builder().id(collegeMaster.getCourse().getId()).name(collegeMaster.getCourse().getCourseName()).build() : null);
        collegeMasterTO.setStateTO(collegeMaster.getState() != null ? StateTO.builder().id(collegeMaster.getState().getId()).name(collegeMaster.getState().getName()).build() : null);
        collegeMasterTO.setDistrictTO(collegeMaster.getDistrict() != null ? DistrictTO.builder().id(collegeMaster.getDistrict().getId()).name(collegeMaster.getDistrict().getName()).build() : null);
        collegeMasterTO.setVillagesTO(collegeMaster.getVillage() != null ? VillagesTO.builder().id(collegeMaster.getVillage().getId()).name(collegeMaster.getVillage().getName()).build() : null);

        CollegeProfile collegeprofile = collegeProfileDaoService.findAdminByCollegeId(id);
        if (collegeprofile != null) {
            BigInteger userId = collegeprofile.getUser().getId();
            User collegeUser = userDaoService.findById(userId);
            collegeMasterTO.setEmailId(collegeUser.getEmail());
            collegeMasterTO.setMobileNumber(collegeUser.getMobileNumber());
        }

        List<UniversityMasterTo> university = universityMasterService.getUniversitiesByCollegeId(id);
        collegeMasterTO.setUniversityTO(university != null && !university.isEmpty() ? UniversityTO.builder().id(university.get(0).getId()).name(university.get(0).getName()).build() : null);

        if (collegeMaster.getStateMedicalCouncil() == null && collegeMaster.getState() != null) {
            StateMedicalCouncil stateMedicalCouncil = stateMedicalCouncilDaoService.findByState(String.valueOf(collegeMaster.getState().getId()));
            collegeMasterTO.setStateMedicalCouncilTO(stateMedicalCouncil != null ? StateMedicalCouncilTO.builder().id(stateMedicalCouncil.getId()).code(stateMedicalCouncil.getCode()).name(stateMedicalCouncil.getName()).build()  : null);
        } else {
            collegeMasterTO.setStateMedicalCouncilTO(StateMedicalCouncilTO.builder().id(collegeMaster.getStateMedicalCouncil().getId()).code(collegeMaster.getStateMedicalCouncil().getCode()).name(collegeMaster.getStateMedicalCouncil().getName()).build());
        }

        return collegeMasterTO;
    }

    private CollegeMaster preCollegeAccessChecks(BigInteger id) throws  NotFoundException {
        User loggedInUser = getLoggedInUser();
        CollegeMaster collegeMaster = collegeMasterDaoService.findById(id);
        if (collegeMaster == null) {
            throw new NotFoundException();
        }

        if (UserSubTypeEnum.COLLEGE_ADMIN.getId().equals(loggedInUser.getUserSubType() != null ? loggedInUser.getUserSubType().getId() : null)) {
            CollegeProfile collegeprofile = collegeProfileDaoService.findAdminByCollegeId(collegeMaster.getId());

            if (collegeprofile == null) {
                throw new NotFoundException(UNSUPPORTED_OPERATION);
            }

            if (!loggedInUser.getId().equals(collegeprofile.getUser().getId())) {
                throw new AccessDeniedException(FORBIDDEN);
            }
        }
        return collegeMaster;
    }

    @Override
    public CollegeResponseTo createOrUpdateCollege(CollegeResponseTo collegeResponseTo) throws NmrException, InvalidRequestException, InvalidIdException, ResourceExistsException, NotFoundException {
        CollegeMaster collegeMaster = null;
        CollegeProfile collegeProfile = null;

        if(!ALLOWED_SUBTYPE_FOR_COLLEGE_UPDATES.contains(getLoggedInUser().getUserSubType().getId())){
            throw new InvalidRequestException();
        }

        if (collegeResponseTo.getId() != null) {
            collegeMaster = collegeMasterDaoService.findById(collegeResponseTo.getId());

            if (collegeMaster == null) {
                throw new NotFoundException();
            }

            collegeProfile = collegeProfileDaoService.findAdminByCollegeId(collegeMaster.getId());
            preCollegeUpdationChecks(collegeResponseTo, collegeMaster, collegeProfile);
        } else {
            duplicateContactsCheck(collegeResponseTo.getEmailId(), collegeResponseTo.getMobileNumber(), UserTypeEnum.COLLEGE.getId());
        }

        collegeMaster = collegeMaster != null ? collegeMaster : new CollegeMaster();
        collegeMaster.setId(collegeResponseTo.getId());
        collegeMaster.setCollegeCode(collegeResponseTo.getCollegeCode());
        collegeMaster.setName(collegeResponseTo.getName());
        collegeMaster.setWebsite(collegeResponseTo.getWebsite());
        collegeMaster.setAddressLine1(collegeResponseTo.getAddressLine1());
        collegeMaster.setAddressLine2(collegeResponseTo.getAddressLine2());
        collegeMaster.setPinCode(collegeResponseTo.getPinCode());
        collegeMaster.setStatus(BigInteger.valueOf(1));
        collegeMaster.setVisibleStatus(BigInteger.valueOf(1));
        collegeMaster.setSystemOfMedicineId(BigInteger.valueOf(1));
        collegeMaster.setCourse(collegeResponseTo.getCourseTO() != null ? new Course(collegeResponseTo.getCourseTO().getId(), collegeResponseTo.getCourseTO().getCourseName()) : collegeMaster.getCourse());
        collegeMaster.setState(State.builder().id(collegeResponseTo.getStateTO() != null ? collegeResponseTo.getStateTO().getId() : collegeMaster.getState().getId()).build());
        collegeMaster.setDistrict(District.builder().id(collegeResponseTo.getDistrictTO() != null ? collegeResponseTo.getDistrictTO().getId() : collegeMaster.getDistrict().getId()).build());
        collegeMaster.setVillage(collegeResponseTo.getVillagesTO() != null ? Villages.builder().id(collegeResponseTo.getVillagesTO().getId()).build() : collegeMaster.getVillage());
        collegeMaster.setStateMedicalCouncil(StateMedicalCouncil.builder().id(collegeResponseTo.getStateMedicalCouncilTO() != null ? collegeResponseTo.getStateMedicalCouncilTO().getId() : collegeMaster.getStateMedicalCouncil().getId()).build());
        collegeMaster = collegeMasterDaoService.save(collegeMaster);
        collegeResponseTo.setId(collegeMaster.getId());

        if (collegeResponseTo.getUniversityTO() != null && collegeResponseTo.getUniversityTO().getId() != null) {
            UniversityMaster university = universityMasterService.findById(collegeResponseTo.getUniversityTO().getId());
            if (university != null && !university.getCollegeId().equals(collegeMaster.getId())) {
                UniversityMaster universityMaster = new UniversityMaster();
                universityMaster.setCollegeId(collegeMaster.getId());
                universityMaster.setName(university.getName());
                universityMaster.setStatus(university.getStatus());
                universityMaster.setVisibleStatus(university.getVisibleStatus());
                universityMasterService.save(universityMaster);
            }
        }

        User user = collegeProfile != null ? collegeProfile.getUser() : null;
        if (user == null) {
            user = new User(null, collegeResponseTo.getEmailId(), collegeResponseTo.getMobileNumber(), null, null, null, false, false,
                    entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getId()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE_ADMIN.getId()),
                    entityManager.getReference(UserGroup.class, Group.COLLEGE.getId()), true, 0, null, null, null, null, false,false, false,null);
        } else {
            user.setEmail(collegeResponseTo.getEmailId());
            user.setMobileNumber(collegeResponseTo.getMobileNumber());
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
        
        passwordService.getResetPasswordLink(new SendLinkOnMailTo(user.getEmail(), user.getUserType().getId()),user.getEmail());
        return collegeResponseTo;
    }

    private CollegeProfile preCollegeUpdationChecks(CollegeResponseTo collegeResponseTo, CollegeMaster collegeMaster, CollegeProfile collegeProfile) throws  ResourceExistsException, NotFoundException {
        User loggedInUser = getLoggedInUser();

        if (collegeMaster == null) {
            throw new NotFoundException();
        }

        if (UserSubTypeEnum.COLLEGE_ADMIN.getId().equals(loggedInUser.getUserSubType() != null ? loggedInUser.getUserSubType().getId() : null)) {
            if (collegeProfile == null) {
                throw new NotFoundException(UNSUPPORTED_OPERATION);
            }

            User user = collegeProfile.getUser();
            if (!collegeResponseTo.getEmailId().equals(user.getEmail())) {
                duplicateEmailCheck(collegeResponseTo.getEmailId(), user.getUserType().getId());
            }

            if (!collegeResponseTo.getMobileNumber().equals(user.getMobileNumber())) {
                duplicateMobileNumberCheck(collegeResponseTo.getMobileNumber(), user.getUserType().getId());
            }

            if (!loggedInUser.getId().equals(collegeProfile.getUser().getId())) {
                throw new AccessDeniedException(FORBIDDEN);
            }
        }
        return collegeProfile;
    }

    @Override
    public List<CollegeMasterDataTO> getAllCollegeVerifiersDesignation() throws NmrException {
        return entityManager.createQuery("select ust from userSubType ust where ust.id != ?1 and ust.userType.id = ?2 order by ust.id asc", UserSubType.class)
                .setParameter(1, UserSubTypeEnum.COLLEGE_ADMIN.getId()).setParameter(2, UserTypeEnum.COLLEGE.getId())
                .getResultList().stream().map(userSubType -> new CollegeMasterDataTO(userSubType.getId(), userSubType.getDisplayName())).toList();
    }

    @Override
    public CollegeProfileTo createOrUpdateCollegeVerifier(CollegeProfileTo collegeProfileTo) throws GeneralSecurityException, NmrException, InvalidRequestException, InvalidIdException, ResourceExistsException {
        CollegeProfile collegeProfile = null;

        if (collegeProfileTo.getId() != null) {
            collegeProfile = collegeProfileDaoService.findById(collegeProfileTo.getId());
            preVerifierUpdationChecks(collegeProfileTo, collegeProfile);
        } else {
            duplicateContactsCheck(collegeProfileTo.getEmailId(), collegeProfileTo.getMobileNumber(), UserTypeEnum.COLLEGE.getId());
        }

        User user = collegeProfile != null ? collegeProfile.getUser() : null;
        if (user == null) {
            user = new User(null, collegeProfileTo.getEmailId(), collegeProfileTo.getMobileNumber(), null,
                    null, null, false, false, entityManager.getReference(UserType.class, UserTypeEnum.COLLEGE.getId()),
                    entityManager.getReference(UserSubType.class, collegeProfileTo.getDesignation()),
                    entityManager.getReference(UserGroup.class, Group.COLLEGE.getId()), true, 0, null, null, null, null, false,false,false, null);
        } else {
            user.setEmail(collegeProfileTo.getEmailId());
            user.setMobileNumber(collegeProfileTo.getMobileNumber());
        }
        user = userDaoService.save(user);

        boolean isNewCollegeVerifierProfile = collegeProfile == null;
        if (isNewCollegeVerifierProfile) {
            collegeProfile = new CollegeProfile();
        }
        collegeProfile.setName(collegeProfileTo.getName());
        collegeProfile.setCollege(entityManager.getReference(CollegeMaster.class, collegeProfileTo.getCollegeId()));
        collegeProfile.setUser(user);
        collegeProfile = collegeProfileDaoService.save(collegeProfile);

        collegeProfileTo.setId(collegeProfile.getId());
        
        if (isNewCollegeVerifierProfile) {
            passwordService.getResetPasswordLink(new SendLinkOnMailTo(user.getEmail(), user.getUserType().getId()),user.getEmail());
        }
        return collegeProfileTo;
    }

    private void duplicateContactsCheck(String emailId, String mobileNumber, BigInteger userType) throws ResourceExistsException {
        duplicateEmailCheck(emailId, userType);
        duplicateMobileNumberCheck(mobileNumber, userType);
    }

    private void duplicateMobileNumberCheck(String mobileNumber, BigInteger userType) throws ResourceExistsException {
        if (userDaoService.existsByMobileNumberAndUserTypeId(mobileNumber, userType)) {
            throw new ResourceExistsException(NMRError.MOBILE_NUM_ALREADY_REGISTERED.getMessage());
        }
    }

    private void duplicateEmailCheck(String emailId, BigInteger userType) throws ResourceExistsException {
        if (userDaoService.existsByEmailAndUserTypeId(emailId, userType)) {
            throw new ResourceExistsException(NMRError.EMAIL_ID_ALREADY_REGISTERED.getMessage());
        }
    }

    private void preVerifierUpdationChecks(CollegeProfileTo collegeProfileTo, CollegeProfile collegeProfile) throws  InvalidIdException, ResourceExistsException {
        User loggedInUser = getLoggedInUser();
        if (collegeProfile == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage());
        }

        User user = collegeProfile.getUser();
        if (!collegeProfileTo.getEmailId().equals(user.getEmail())) {
            duplicateEmailCheck(collegeProfileTo.getEmailId(), user.getUserType().getId());
        }

        if (!collegeProfileTo.getMobileNumber().equals(user.getMobileNumber())) {
            duplicateMobileNumberCheck(collegeProfileTo.getMobileNumber(), user.getUserType().getId());
        }

        if (!loggedInUser.getId().equals(collegeProfile.getUser().getId())) {
            throw new AccessDeniedException(FORBIDDEN);
        }

        if (!collegeProfileTo.getCollegeId().equals(collegeProfile.getCollege().getId())) {
            throw new AccessDeniedException(FORBIDDEN);
        }
    }

    @Override
    public CollegeProfileTo getCollegeVerifier(BigInteger collegeId, BigInteger verifierId) throws NmrException, InvalidIdException {
        CollegeProfile collegeProfile = preVerifierAccessChecks(collegeId, verifierId);

        CollegeProfileTo collegeProfileTo = new CollegeProfileTo();
        collegeProfileTo.setId(collegeProfile.getId());
        collegeProfileTo.setName(collegeProfile.getName());
        collegeProfileTo.setCollegeId(collegeProfile.getCollege().getId());

        User user = collegeProfile.getUser();
        collegeProfileTo.setEmailId(user.getEmail());
        collegeProfileTo.setMobileNumber(user.getMobileNumber());
        collegeProfileTo.setDesignation(user.getUserSubType().getId());
        return collegeProfileTo;
    }

    private CollegeProfile preVerifierAccessChecks(BigInteger collegeId, BigInteger verifierId) throws InvalidIdException {
        User loggedInUser = getLoggedInUser();
        CollegeProfile collegeProfile = collegeProfileDaoService.findById(verifierId);

        if (collegeProfile == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage());
        }

        if (!loggedInUser.getId().equals(collegeProfile.getUser().getId())) {
            throw new AccessDeniedException(FORBIDDEN);
        }

        if (!collegeId.equals(collegeProfile.getCollege().getId())) {
            throw new AccessDeniedException(FORBIDDEN);
        }
        return collegeProfile;
    }

    @Override
    public User getLoggedInUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();

        return userDaoService.findByUsername(userName, userType);
    }
}
