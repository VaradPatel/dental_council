package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

public class CommonTestData {

    public static final String EMAIL_ID = "test@gmail.com";
    public static final String NMR_ID = "123456789012";
    public static final String HPR_ID = "test@hpr.abdm";
    public static final String HPR_NUMBER = "123456789012";
    public static final BigInteger ID =  BigInteger.valueOf(1);
    public static final BigInteger STATE_ID =  BigInteger.valueOf(20);
    public static final String STATE_NAME = "Maharashtra";
    public static final String STATE_MEDICAL_COUNCIL = "Maharashtra Medical Council";
    public static final String ISO_CODE = "20";
    public static final String STATE_SHORT_NAME = "MAH";
    public static final String PROFILE_DISPLAY_NAME = "John Doe";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String MIDDLE_NAME = "John";
    public static final Integer ENROLLED_NUMBER = 1;
    public static final Integer NDHM_ENROLLMENT_NUMBER = 1;
    public static final String MOBILE_NUMBER = "9090909090";
    public static final String SUPER_SPECIALITY = "MS";
    public static final String REQUEST_ID = "NMR1001";
    public static final String PROFILE_PHOTO = "Base 64";
    public static final Date DATE_OF_BIRTH =  Date.valueOf("1990-12-01");
    public static final Date REGISTRATION_DATE =  Date.valueOf("1990-12-01");
    public static final String REGISTRATION_YEAR= "1990";
    public static final String REGISTRATION_NUMBER = "MAH-123";
    public static final String BROAD_SPECIALITY = "BS";
    public static final String COLLEGE_CODE = "123";
    public static final String COURSE_NAME = "MBBS";
    public static final String DISTRICT_NAME = "Pune";
    public static final String COLLEGE_NAME = "Govt. college of science, Akola";
    public static final String ADDRESS_LINE_1 = "Line1";
    public static final String ADDRESS_LINE_2 = "Line2";
    public static final String PIN_CODE = "123456";
    public static final BigInteger COLLEGE_STATUS = BigInteger.valueOf(1);
    public static final BigInteger COLLEGE_VISIBLE_STATUS = BigInteger.valueOf(1);
    public static final BigInteger SYSTEM_OF_MEDICINE = BigInteger.valueOf(1);
    public static final String WEBSITE = "abc@xyz.com";
    public static final String VILLAGE_NAME = "Pune";
    public static final String SUB_DISTRICT_NAME = "Pune";
    public static final String LANGUAGE_NAME = "English";
    public static final String PERMANENT_RENEWATION = "Permanent";
    public static final String QUERY_COMMENT = "Comment";
    public static final String QUERY_ON = "Name";
    public static final String QUERY_SECTION = "Personal";

    public static UserGroup getUserGroup(BigInteger userGroupId){
        Group group = Group.getGroup(userGroupId);
        UserGroup userGroup =  new UserGroup();
        userGroup.setId(group.getId());
        userGroup.setName(group.getDescription());
        userGroup.setApplicationPendencyInDays("0");
        return userGroup;
    }

    public static UserSubType getUserSubType(BigInteger userSubTypeId){
        UserSubTypeEnum userSubTypeEnum = UserSubTypeEnum.getUserSubType(userSubTypeId);
        UserSubType userSubType =  new UserSubType();
        userSubType.setGroup(getUserGroup(userSubTypeEnum.getGroup().getId()));
        userSubType.setName(userSubTypeEnum.getName());
        userSubType.setId(userSubTypeEnum.getCode());
        userSubType.setUserType(getUserType(userSubTypeEnum.getUserType()));
        return userSubType;
    }

    public static UserType getUserType(BigInteger userTypeId){
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserSubType(userTypeId);
        UserType userType =  new UserType();
        userType.setId(userTypeEnum.getCode());
        userType.setName(userType.getName());
        userType.setGroup(getUserGroup(userTypeEnum.getGroup().getId()));
        return userType;
    }

    public static UserType getUserType(String userTypeName){
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserSubType(userTypeName);
        UserType userType =  new UserType();
        userType.setId(userTypeEnum.getCode());
        userType.setName(userType.getName());
        userType.setGroup(getUserGroup(userTypeEnum.getGroup().getId()));
        return userType;
    }

    public static StateMedicalCouncil getStateMedicalCouncil() {
        StateMedicalCouncil stateMedicalCouncil = new StateMedicalCouncil();
        stateMedicalCouncil.setState(STATE_NAME);
        stateMedicalCouncil.setId(ID);
        stateMedicalCouncil.setCode(ISO_CODE);
        stateMedicalCouncil.setNameShort(STATE_SHORT_NAME);
        stateMedicalCouncil.setName(STATE_MEDICAL_COUNCIL);
        return stateMedicalCouncil;
    }

    public static List<StateMedicalCouncilTO> getStateMedicalCouncilTo(){
        return IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(List.of(getStateMedicalCouncil()));
    }

    public static User getUser(BigInteger userTypeId) {
        User user =  new User();
        user.setEmail(EMAIL_ID);
        user.setAccountNonLocked(true);
        UserType userType = getUserType(userTypeId);
        user.setUserType(userType);
        user.setFailedAttempt(0);
        user.setHprId(HPR_ID);
        user.setNmrId(NMR_ID);
        user.setEmailNotificationEnabled(true);
        user.setEmailVerified(true);
        user.setGroup(userType.getGroup());
        user.setId(ID);
        return user;
    }

    public static List<SuperSpeciality> getSuperSpeciality(){
        SuperSpeciality superSpeciality =  new SuperSpeciality();
        superSpeciality.setName(SUPER_SPECIALITY);
        superSpeciality.setId(ID);
        superSpeciality.setHpProfileId(ID);
        return List.of(superSpeciality);
    }

    public static Country getCountry(){
        Country  country =  new Country();
        country.setId(ID);
        country.setName(NMRConstants.INDIA);
        return country;
    }

    public static State getState(){
        State state =  new State();
        state.setCountry(getCountry());
        state.setId(STATE_ID);
        state.setIsoCode(ISO_CODE);
        state.setName(STATE_NAME);
        return state;
    }

    public static HpProfile getHpProfile(){
        HpProfile hpProfile = new HpProfile();
        hpProfile.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.PENDING.getId()).build());
        hpProfile.setId(ID);
        hpProfile.setFullName(PROFILE_DISPLAY_NAME);
        hpProfile.setMobileNumber(MOBILE_NUMBER);
        hpProfile.setESignStatus(NMRConstants.SUCCESS);
        hpProfile.setIsNew(0);
        hpProfile.setRequestId(REQUEST_ID);
        hpProfile.setNmrId(NMR_ID);
        hpProfile.setCountryNationality(getCountry());
        hpProfile.setEmailId(EMAIL_ID);
        hpProfile.setProfilePhoto(PROFILE_PHOTO);
        hpProfile.setDateOfBirth(DATE_OF_BIRTH);
        hpProfile.setGender("M");
        return hpProfile;
    }

    public static BroadSpeciality getBroadSpeciality(){
        BroadSpeciality broadSpeciality = new BroadSpeciality();
        broadSpeciality.setId(ID);
        broadSpeciality.setName(BROAD_SPECIALITY);
        return broadSpeciality;
    }

    public static Course getCourse(){
        Course course = new Course();
        course.setCourseName(COURSE_NAME);
        course.setId(ID);
        return course;
    }

    public static District getDistrict(){
        District district =  new District();
        district.setId(ID);
        district.setName(DISTRICT_NAME);
        district.setIsoCode(ISO_CODE);
        district.setState(getState());
        return district;
    }

    public static CollegeMaster getCollegeMaster(){
        CollegeMaster collegeMaster =  new CollegeMaster();
        collegeMaster.setId(ID);
        collegeMaster.setCollegeCode(COLLEGE_CODE);
        collegeMaster.setCourse(getCourse());
        collegeMaster.setDistrict(getDistrict());
        collegeMaster.setName(COLLEGE_NAME);
        collegeMaster.setAddressLine1(ADDRESS_LINE_1);
        collegeMaster.setAddressLine2(ADDRESS_LINE_2);
        collegeMaster.setPinCode(PIN_CODE);
        collegeMaster.setStateMedicalCouncil(getStateMedicalCouncil());
        collegeMaster.setStatus(COLLEGE_STATUS);
        collegeMaster.setVisibleStatus(COLLEGE_VISIBLE_STATUS);
        collegeMaster.setSystemOfMedicineId(SYSTEM_OF_MEDICINE);
        collegeMaster.setVillage(getVillage());
        collegeMaster.setWebsite(WEBSITE);
        collegeMaster.setState(getState());
        return collegeMaster;
    }

    public static Villages getVillage() {
        Villages villages =  new Villages();
        villages.setId(ID);
        villages.setName(VILLAGE_NAME);
        villages.setSubdistrict(getSubDistrict());
        villages.setIsoCode(ISO_CODE);
        return villages;
    }

    public static SubDistrict getSubDistrict() {
        SubDistrict subDistrict =  new SubDistrict();
        subDistrict.setIsoCode(ISO_CODE);
        subDistrict.setName(SUB_DISTRICT_NAME);
        subDistrict.setId(ID);
        subDistrict.setDistrictCode(getDistrict());
        return subDistrict;
    }

    public static Language getLanguage(){
        Language language = new Language();
        language.setId(ID);
        language.setName(LANGUAGE_NAME);
        return language;
    }

    public static HpProfileMaster getMasterHpProfile(){
        HpProfileMaster hpProfileMaster = new HpProfileMaster();
        hpProfileMaster.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.PENDING.getId()).build());
        hpProfileMaster.setId(ID);
        hpProfileMaster.setProfilePhoto(PROFILE_PHOTO);
        hpProfileMaster.setCountryNationality(getCountry());
        hpProfileMaster.setFullName(PROFILE_DISPLAY_NAME);
        hpProfileMaster.setMobileNumber(MOBILE_NUMBER);
        hpProfileMaster.setRequestId(REQUEST_ID);
        hpProfileMaster.setNmrId(NMR_ID);
        hpProfileMaster.setCountryNationality(getCountry());
        hpProfileMaster.setEmailId(EMAIL_ID);
        hpProfileMaster.setProfilePhoto(PROFILE_PHOTO);
        hpProfileMaster.setDateOfBirth(DATE_OF_BIRTH);
        hpProfileMaster.setGender("M");
        return hpProfileMaster;
    }

    public static RegistrationDetailsMaster getMasterRegistrationDetails(){
        RegistrationDetailsMaster registrationDetailsMaster =  new RegistrationDetailsMaster();
        registrationDetailsMaster.setId(1);
        registrationDetailsMaster.setHpProfileMaster(getMasterHpProfile());
        registrationDetailsMaster.setRegistrationNo(REGISTRATION_NUMBER);
        registrationDetailsMaster.setRegistrationDate(REGISTRATION_DATE);
        registrationDetailsMaster.setStateMedicalCouncil(getStateMedicalCouncil());
        return registrationDetailsMaster;

    }

    public static RegistrationRenewationType getRegistrationRenewationType(){
        RegistrationRenewationType registrationRenewationType =  new RegistrationRenewationType();
        registrationRenewationType.setId(ID);
        registrationRenewationType.setName(PERMANENT_RENEWATION);
        return registrationRenewationType;
    }
}
