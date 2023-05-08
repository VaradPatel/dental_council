package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.entity.RegistrationsDetails;
import in.gov.abdm.nmr.redis.hash.Otp;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;

@UtilityClass
public final class CommonTestData {

    public static final String EMAIL_ID = "test@gmail.com";
    public static final String NMR_ID = "123456789012";
    public static final String HPR_ID = "test@hpr.abdm";
    public static final String HPR_NUMBER = "123456789012";
    public static final BigInteger ID = BigInteger.valueOf(1);
    public static final BigInteger STATE_ID = BigInteger.valueOf(20);
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
    public static final Date DATE_OF_BIRTH = Date.valueOf("1990-12-01");
    public static final Date REGISTRATION_DATE = Date.valueOf("1990-12-01");
    public static final String REGISTRATION_YEAR = "1990";
    public static final String REGISTRATION_NUMBER = "MAH-123";
    public static final String BROAD_SPECIALITY = "BS";
    public static final String COLLEGE_CODE = "123";
    public static final BigInteger COURSE_ID = BigInteger.valueOf(1);
    public static final String COURSE_NAME = "MBBS";
    public static final BigInteger DISTRICT_ID = BigInteger.valueOf(1);
    public static final String DISTRICT_NAME = "Pune";
    public static final String DISTRICT_CODE = "Pune";
    public static final BigInteger COLLEGE_ID = BigInteger.valueOf(1);
    public static final String COLLEGE_NAME = "Govt. college of science, Akola";
    public static final BigInteger DESIGNATION = BigInteger.valueOf(1);
    public static final Integer ADDRESS_TYPE_ID = 1;
    public static final String ADDRESS_TYPE_NAME = "Line1";
    public static final String ADDRESS_LINE_1 = "Line1";
    public static final String ADDRESS_LINE_2 = "Line2";
    public static final String PIN_CODE = "123456";
    public static final String HOUSE = "Tech Park";
    public static final String STREET = "Laxmi Society";
    public static final String LOCALITY = "Model Colony";
    public static final String LANDMARK = "Icc Trade Tower";
    public static final String LATITUDE = "10.990";
    public static final String LONGITUDE = "10.990";
    public static final BigInteger COLLEGE_STATUS = BigInteger.valueOf(1);
    public static final BigInteger COLLEGE_VISIBLE_STATUS = BigInteger.valueOf(1);
    public static final BigInteger SYSTEM_OF_MEDICINE = BigInteger.valueOf(1);
    public static final String SYSTEM_OF_MEDICINE_CODE = "abcd";
    public static final String WEBSITE = "abc@xyz.com";
    public static final BigInteger VILLAGE_ID = BigInteger.valueOf(1);
    public static final String VILLAGE_NAME = "Pune";
    public static final String VILLAGE_CODE = "490";

    public static final BigInteger SUB_DISTRICT_ID = BigInteger.valueOf(1);

    public static final String SUB_DISTRICT_NAME = "Pune";
    public static final String SUB_DISTRICT_CODE = "490";
    public static final String LANGUAGE_NAME = "English";
    public static final String PERMANENT_RENEWATION = "Permanent";
    public static final String QUERY_COMMENT = "Comment";
    public static final String QUERY_ON = "Name";
    public static final String QUERY_SECTION = "Personal";
    public static final String GENDER = "M";
    public static final String TRANSACTION_ID = "6ce5d7cb-bf2d-4b0b-9b54-e91a996690bc";
    public static final String TYPE = "SMS";
    public static final BigInteger USER_ID = BigInteger.valueOf(1);
    public static final BigInteger SMC_ID = BigInteger.valueOf(14);
    public static final BigInteger UNIVERSITY_ID = BigInteger.valueOf(1);
    public static final String UNIVERSITY_NAME = "Maharashtra University";
    public static final String SMC_CODE = "MAH";
    public static final String SMC_NAME = "MAH";
    public static final String TEMP_TOKN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.";
    public static final String TEST_PSWD = "Test@123";
    public static final String FACILITY_ID = "1";
    public static final String FACILITY_NAME = "1";
    public static final String FACILITY_STATUS = "1";
    public static final String FACILITY_CODE = "1";
    public static final String FACILITY_TYPE = "1";
    public static final String OWNERSHIP_CODE = "1";
    public static final String OWNERSHIP = "1";
    public static final String STATE_CODE = "1";
    public static final String OTP = "738775";
    public static final String TEST_USER = "user1";
    public static final String PASSWORD = "Test@123";
    public static final String LAST_FOUR_DIGIT_AADHAAR = "1859";

    public static final BigInteger HP_ID = BigInteger.valueOf(1);
    public static final String HP_NAME = "MAH";
    public static final BigInteger COUNTRY_ID = BigInteger.valueOf(1);
    public static final String COUNTRY_NAME = "INDIA";
    public static final BigInteger SCHEDULE_ID = BigInteger.valueOf(1);
    public static final String SCHEDULE_NAME = "schedule 1";
    public static final String CURRENT_DATE = String.valueOf(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    public static final Timestamp CURRENT_TIMESTAMP = new Timestamp(Calendar.getInstance().getTimeInMillis());
    public static final Timestamp FUTURE_TIMESTAMP = Timestamp.valueOf(CURRENT_TIMESTAMP.toLocalDateTime().plusDays(14));
    public static final Timestamp PAST_TIMESTAMP = Timestamp.valueOf(CURRENT_TIMESTAMP.toLocalDateTime().minusDays(14));

    public static final String DEGREE = "MBBS";
    public static final String SALUTATION_DR = "Dr.";
    public static final String CLIENT_ID = "NMR_CLIENT_ID";
    public static final String CLIENT_SECRET = "NMR_CLIENT_SECRET";

    public static UserGroup getUserGroup(BigInteger userGroupId) {
        Group group = Group.getGroup(userGroupId);
        UserGroup userGroup = new UserGroup();
        userGroup.setId(group.getId());
        userGroup.setName(group.getDescription());
        userGroup.setApplicationPendencyInDays("0");
        return userGroup;
    }

    public static UserSubType getUserSubType(BigInteger userSubTypeId) {
        UserSubTypeEnum userSubTypeEnum = UserSubTypeEnum.getUserSubType(userSubTypeId);
        UserSubType userSubType = new UserSubType();
        userSubType.setGroup(getUserGroup(userSubTypeEnum.getGroup().getId()));
        userSubType.setName(userSubTypeEnum.getName());
        userSubType.setId(userSubTypeEnum.getCode());
        userSubType.setUserType(getUserType(userSubTypeEnum.getUserType()));
        return userSubType;
    }

    public static UserType getUserType(BigInteger userTypeId) {
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserSubType(userTypeId);
        UserType userType = new UserType();
        userType.setId(userTypeEnum.getCode());
        userType.setName(userType.getName());
        userType.setGroup(getUserGroup(userTypeEnum.getGroup().getId()));
        return userType;
    }

    public static UserType getUserType(String userTypeName) {
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserSubType(userTypeName);
        UserType userType = new UserType();
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

    public static List<StateMedicalCouncilTO> getStateMedicalCouncilTo() {
        return IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(List.of(getStateMedicalCouncil()));
    }

    public static User getUser(BigInteger userTypeId) {
        User user = new User();
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
        user.setPassword(TEST_PSWD);
        return user;
    }

    public static List<SuperSpeciality> getSuperSpeciality() {
        SuperSpeciality superSpeciality = new SuperSpeciality();
        superSpeciality.setName(SUPER_SPECIALITY);
        superSpeciality.setId(ID);
        superSpeciality.setHpProfileId(ID);
        return List.of(superSpeciality);
    }

    public static Country getCountry() {
        Country country = new Country();
        country.setId(ID);
        country.setName(NMRConstants.INDIA);
        return country;
    }

    public static State getState() {
        State state = new State();
        state.setCountry(getCountry());
        state.setId(STATE_ID);
        state.setIsoCode(ISO_CODE);
        state.setName(STATE_NAME);
        return state;
    }

    public static HpProfile getHpProfile() {
        HpProfile hpProfile = new HpProfile();
        hpProfile.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.PENDING.getId()).build());
        hpProfile.setId(ID);
        hpProfile.setFullName(PROFILE_DISPLAY_NAME);
        hpProfile.setMobileNumber(MOBILE_NUMBER);
        hpProfile.setESignStatus(NMRConstants.SUCCESS);
        hpProfile.setIsNew(0);
        hpProfile.setRequestId(REQUEST_ID);
        //hpProfile.setNmrId(NMR_ID);
        hpProfile.setCountryNationality(getCountry());
        hpProfile.setEmailId(EMAIL_ID);
        hpProfile.setProfilePhoto(PROFILE_PHOTO);
        hpProfile.setDateOfBirth(DATE_OF_BIRTH);
        hpProfile.setGender("M");
        hpProfile.setTransactionId(TRANSACTION_ID);
        hpProfile.setRegistrationId(REGISTRATION_NUMBER);
        hpProfile.setUser(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));

        return hpProfile;
    }

    public static BroadSpeciality getBroadSpeciality() {
        BroadSpeciality broadSpeciality = new BroadSpeciality();
        broadSpeciality.setId(ID);
        broadSpeciality.setName(BROAD_SPECIALITY);
        return broadSpeciality;
    }

    public static Course getCourse() {
        Course course = new Course();
        course.setCourseName(COURSE_NAME);
        course.setId(ID);
        return course;
    }

    public static District getDistrict() {
        District district = new District();
        district.setId(ID);
        district.setName(DISTRICT_NAME);
        district.setIsoCode(ISO_CODE);
        district.setState(getState());
        return district;
    }

    public static CollegeMaster getCollegeMaster() {
        CollegeMaster collegeMaster = new CollegeMaster();
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
        Villages villages = new Villages();
        villages.setId(ID);
        villages.setName(VILLAGE_NAME);
        villages.setSubdistrict(getSubDistrict());
        villages.setIsoCode(ISO_CODE);
        return villages;
    }

    public static SubDistrict getSubDistrict() {
        SubDistrict subDistrict = new SubDistrict();
        subDistrict.setIsoCode(ISO_CODE);
        subDistrict.setName(SUB_DISTRICT_NAME);
        subDistrict.setId(ID);
        subDistrict.setDistrictCode(getDistrict());
        return subDistrict;
    }

    public static Language getLanguage() {
        Language language = new Language();
        language.setId(ID);
        language.setName(LANGUAGE_NAME);
        return language;
    }

    public static HpProfileMaster getMasterHpProfile() {
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

    public static RegistrationDetailsMaster getMasterRegistrationDetails() {
        RegistrationDetailsMaster registrationDetailsMaster = new RegistrationDetailsMaster();
        registrationDetailsMaster.setId(1);
        registrationDetailsMaster.setHpProfileMaster(getMasterHpProfile());
        registrationDetailsMaster.setRegistrationNo(REGISTRATION_NUMBER);
        registrationDetailsMaster.setRegistrationDate(REGISTRATION_DATE);
        registrationDetailsMaster.setStateMedicalCouncil(getStateMedicalCouncil());
        return registrationDetailsMaster;

    }

    public static RegistrationRenewationType getRegistrationRenewationType() {
        RegistrationRenewationType registrationRenewationType = new RegistrationRenewationType();
        registrationRenewationType.setId(ID);
        registrationRenewationType.setName(PERMANENT_RENEWATION);
        return registrationRenewationType;
    }

    public static Council getImrCouncilDetails() {
        Council council = new Council();
        council.setFullName(PROFILE_DISPLAY_NAME);
        council.setGender(GENDER);
        council.setDateOfBirth(DATE_OF_BIRTH.toString());
        RegistrationsDetails registrationsDetails = new RegistrationsDetails();
        registrationsDetails.setCouncilName(STATE_MEDICAL_COUNCIL);
        registrationsDetails.setRegistrationNo(REGISTRATION_NUMBER);
        council.setRegistrationsDetails(List.of(registrationsDetails));
        return council;
    }

    public static Address getCommunicationAddress() {
        Address address = new Address();
        address.setAddressLine1(ADDRESS_LINE_1);
        AddressType addressType = new AddressType();
        addressType.setId(in.gov.abdm.nmr.enums.AddressType.COMMUNICATION.getId());
        addressType.setType(in.gov.abdm.nmr.enums.AddressType.COMMUNICATION.getDescription());
        address.setAddressTypeId(addressType);
        address.setMobile(MOBILE_NUMBER);
        address.setCountry(getCountry());
        address.setHpProfileId(ID);
        address.setHouse(HOUSE);
        address.setDistrict(getDistrict());
        address.setLandmark(LANDMARK);
        address.setEmail(EMAIL_ID);
        address.setPincode(PIN_CODE);
        address.setState(getState());
        address.setLocality(LOCALITY);
        address.setVillage(getVillage());
        address.setStreet(STREET);
        address.setSubDistrict(getSubDistrict());
        return address;
    }

    public INextGroup getNextGroup() {
        return new INextGroup() {
            @Override
            public BigInteger getAssignTo() {
                return Group.SMC.getId();
            }

            @Override
            public BigInteger getWorkFlowStatusId() {
                return WorkflowStatus.PENDING.getId();
            }
        };
    }

    public HpProfileStatus getHPProfileStatus() {
        return HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.PENDING.getId()).build();
    }

    public WorkFlow getWorkFlow() {
        WorkFlow workFlow = new WorkFlow();
        workFlow.setId(ID);
        workFlow.setApplicationType(new ApplicationType(ID, HP_NAME, "desc", ""));
        return workFlow;
    }

    public WorkFlowRequestTO getWorkFlowRequestTO() {
        WorkFlowRequestTO workFlowRequestTO = new WorkFlowRequestTO();
        workFlowRequestTO.setRequestId(REQUEST_ID);
        workFlowRequestTO.setHpProfileId(getHpProfile().getId());
        workFlowRequestTO.setApplicationTypeId(in.gov.abdm.nmr.enums.ApplicationType.HP_REGISTRATION.getId());
        workFlowRequestTO.setActorId(Group.HEALTH_PROFESSIONAL.getId());
        workFlowRequestTO.setActionId(in.gov.abdm.nmr.enums.Action.SUBMIT.getId());
        workFlowRequestTO.setRemarks("Doctor Registration");
        workFlowRequestTO.setApplicationSubTypeId(BigInteger.valueOf(1));
        return workFlowRequestTO;
    }

    public ForeignQualificationDetails getForeignQualificationDetails() {
        ForeignQualificationDetails foreignQualificationDetails = new ForeignQualificationDetails();
        foreignQualificationDetails.setId(ID);
        return foreignQualificationDetails;
    }


    public ForeignQualificationDetailsMaster getForeignQualificationDetailsMaster() {
        ForeignQualificationDetailsMaster foreignQualificationDetailsMaster = new ForeignQualificationDetailsMaster();
        foreignQualificationDetailsMaster.setId(ID);
        foreignQualificationDetailsMaster.setCourse(COURSE_NAME);
        return foreignQualificationDetailsMaster;
    }

    public SMCProfileTO getSMCProfile() {
        SMCProfileTO smcProfile = new SMCProfileTO();
        smcProfile.setId(ID);
        return smcProfile;
    }

    public static UniversityMaster getUniversityMaster() {
        UniversityMaster universityMaster = new UniversityMaster();
        universityMaster.setId(ID);
        return universityMaster;
    }

    public static Password getPassword() {
        Password password = new Password();
        password.setId(ID);
        return password;
    }

    public static ResponseMessageTo getResponseMessage() {
        ResponseMessageTo message = new ResponseMessageTo();
        message.setMessage(SUCCESS_RESPONSE);
        return message;
    }

    public SendLinkOnMailTo getSendLinkOnMail() {
        SendLinkOnMailTo linkOnMailTo = new SendLinkOnMailTo();
        linkOnMailTo.setEmail(CommonTestData.EMAIL_ID);
        return linkOnMailTo;
    }

    public SetNewPasswordTo getSetNewPasswordTo() {
        SetNewPasswordTo newPassword = new SetNewPasswordTo();
        newPassword.setToken(CommonTestData.TEMP_TOKN);
        newPassword.setPassword(CommonTestData.PASSWORD);
        return newPassword;
    }

    public ResetToken getResetToken() {
        ResetToken resetToken = new ResetToken();
        // resetToken.setToken(TEMP_TOKN);
        resetToken.setId(CommonTestData.USER_ID);
        resetToken.setExpiryDate(FUTURE_TIMESTAMP);
        resetToken.setUserName(TEST_USER);
        return resetToken;
    }

    public ResetPasswordRequestTo getResetPasswordRequest() {
        ResetPasswordRequestTo resetPasswordRequest = new ResetPasswordRequestTo();
        resetPasswordRequest.setUsername(TEST_USER);
        resetPasswordRequest.setUsername(TEST_PSWD);
        resetPasswordRequest.setTransactionId(TRANSACTION_ID);
        return resetPasswordRequest;
    }

    public FacilitySearchResponseTO getFacilitySearchResponseTO() {
        FacilitySearchResponseTO facilitySearchResponseTO = new FacilitySearchResponseTO();
        facilitySearchResponseTO.setTotalFacilities(1);
        return facilitySearchResponseTO;
    }

    public SessionResponseTo getSessionResponse() {
        SessionResponseTo sessionResponse = new SessionResponseTo();
        sessionResponse.setAccessToken(TEMP_TOKN);
        return sessionResponse;
    }


}
