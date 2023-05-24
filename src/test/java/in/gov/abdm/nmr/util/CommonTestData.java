package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.entity.Action;
import in.gov.abdm.nmr.entity.AddressType;
import in.gov.abdm.nmr.entity.ApplicationType;
import in.gov.abdm.nmr.entity.HpProfileStatus;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.entity.RegistrationsDetails;
import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.security.common.RoleConstants;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@UtilityClass
public final class
CommonTestData {

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
    public static final String FULL_NAME = "John Doe John";
    public static final Integer ENROLLED_NUMBER = 1;
    public static final Integer NDHM_ENROLLMENT_NUMBER = 1;
    public static final String MOBILE_NUMBER = "9090909090";
    public static final String SUPER_SPECIALITY = "MS";
    public static final String REQUEST_ID = "NMR1001";
    public static final String PROFILE_PHOTO = "Base 64";
    public static final String CAPTCHA_IMAGE = "Base 64";
    public static final Date DATE_OF_BIRTH = Date.valueOf("1990-12-01");
    public static final Date REGISTRATION_DATE = Date.valueOf("1990-12-01");
    public static final String REGISTRATION_YEAR = "1990";
    public static final String REGISTRATION_NUM_IN_LOWER_CASE = "registrationNumber";
    public static final String REGISTRATION_NUMBER = "MAH-123";
    public static final String BROAD_SPECIALITY = "BS";
    public static final String NAME = "NAME";
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
    public static final String PIN_CODE = "444001";
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

    public static final String REGISTRATION_CERTIFICATE = "registration certificate";
    public static final String FILE_NAME = "File Name";
    public static final String FILE_TYPE = "pdf";
    private static final String YEAR_OF_PASSING = "2023";
    private static final String MONTH_OF_PASSING = "MAY";
    private static final String PASSPORT_NO = "123456789012";
    private static final String ROLL_NO = "1234567890";
    private static final String RESULT = "USER_RESULT";
    private static final String QUALIFICATION_FROM = "India";
    public static final Integer CAPTCHA_RESULT = 20;
    public static final String IP_ADDRESS = "35.166.48.97";

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
        userSubType.setId(userSubTypeEnum.getId());
        userSubType.setUserType(getUserType(userSubTypeEnum.getUserType().getId()));
        userSubType.setRoles(RoleConstants.STATE_MEDICAL_COUNCIL_ADMIN);
        return userSubType;
    }

    public static UserType getUserType(BigInteger userTypeId) {
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserSubType(userTypeId);
        UserType userType = new UserType();
        userType.setId(userTypeEnum.getId());
        userType.setName(userType.getName());
        userType.setRoles(RoleConstants.STATE_MEDICAL_COUNCIL_ADMIN);
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

    public static List<StateMedicalCouncilTO> getStateMedicalCouncilTos() {
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
        user.setUserSubType(getUserSubType(UserSubTypeEnum.SMC_ADMIN.getId()));
        user.setMobileNumber(MOBILE_NUMBER);
        user.setEmailVerified(false);
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
        hpProfile.setUser(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));

        return hpProfile;
    }

    public static HpProfile getHpProfileInApprovedStatus() {
        HpProfile hpProfile = new HpProfile();
        hpProfile.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.APPROVED.getId()).build());
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
        hpProfile.setUser(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));

        return hpProfile;
    }

    public static BroadSpeciality getBroadSpeciality() {
        BroadSpeciality broadSpeciality = new BroadSpeciality();
        broadSpeciality.setId(ID);
        broadSpeciality.setName(BROAD_SPECIALITY);
        return broadSpeciality;
    }

    public static List<BroadSpecialityTO> getBroadSpecialityTo() {
        List<BroadSpecialityTO> broadSpecialityTOList = new ArrayList<>();
        BroadSpecialityTO broadSpeciality = new BroadSpecialityTO();
        broadSpeciality.setId(ID);
        broadSpeciality.setName(NAME);
        broadSpecialityTOList.add(broadSpeciality);
        return broadSpecialityTOList;
    }

    public static List<CountryTO> getCountryTo() {
        List<CountryTO> countryTOS = new ArrayList<>();
        CountryTO countryTO = new CountryTO();
        countryTO.setId(ID);
        countryTO.setName(NAME);
        countryTOS.add(countryTO);
        return countryTOS;
    }

    public static List<DistrictTO> getDistricts() {
        return List.of(getDistrictTO());
    }

    public static List<StateTO> getStates() {
        return List.of(getStateTO());
    }

    public static List<SubDistrictTO> getSubDistricts() {
        return List.of(getSubDistrictTO());
    }

    public static List<VillagesTO> getVillages() {
        return List.of(getVillageTO());
    }

    public static List<LanguageTO> getLanguangesTo() {
        List<LanguageTO> languageTOS = new ArrayList<>();
        LanguageTO languageTO = new LanguageTO();
        languageTO.setId(ID);
        languageTO.setName(NAME);
        languageTOS.add(languageTO);
        return languageTOS;
    }

    public static List<CourseTO> getCourses() {
        List<CourseTO> courseTOS = new ArrayList<>();
        CourseTO courseTO = new CourseTO();
        courseTO.setId(ID);
        courseTO.setName(NAME);
        courseTOS.add(courseTO);
        return courseTOS;
    }

    public static CourseTO getCourseTo() {
        CourseTO courseTO = new CourseTO();
        courseTO.setId(ID);
        courseTO.setName(NAME);
        return courseTO;
    }

    public static CountryTO getCountryTO() {
        CountryTO countryTO = new CountryTO();
        countryTO.setId(ID);
        countryTO.setName(NAME);
        return countryTO;
    }

    public static DistrictTO getDistrictTO() {
        DistrictTO districtTo = new DistrictTO();
        districtTo.setId(ID);
        districtTo.setName(NAME);
        return districtTo;
    }

    public static StateTO getStateTO() {
        StateTO stateTO = new StateTO();
        stateTO.setId(ID);
        stateTO.setName(NAME);
        return stateTO;
    }

    public static SubDistrictTO getSubDistrictTO() {
        SubDistrictTO subDistrictTO = new SubDistrictTO();
        subDistrictTO.setId(ID);
        subDistrictTO.setName(NAME);
        return subDistrictTO;
    }

    public static VillagesTO getVillageTO() {
        VillagesTO villagesTO = new VillagesTO();
        villagesTO.setId(ID);
        villagesTO.setName(NAME);
        return villagesTO;
    }


    public static List<RegistrationRenewationTypeTO> getRegistrationRenewationTypeTO() {
        List<RegistrationRenewationTypeTO> registrationRenewationTypeTOS = new ArrayList<>();
        RegistrationRenewationTypeTO registrationRenewationTypeTO = new RegistrationRenewationTypeTO();
        registrationRenewationTypeTO.setId(ID);
        registrationRenewationTypeTO.setName(NAME);
        registrationRenewationTypeTOS.add(registrationRenewationTypeTO);
        return registrationRenewationTypeTOS;
    }

    public static List<FacilityTypeTO> getFacilityTypeTO() {
        List<FacilityTypeTO> facilityTypeTOS = new ArrayList<>();
        FacilityTypeTO facilityTypeTO = new FacilityTypeTO();
        facilityTypeTO.setId(ID);
        facilityTypeTO.setName(NAME);
        facilityTypeTOS.add(facilityTypeTO);
        return facilityTypeTOS;
    }

    public static List<MasterDataTO> getListOfMasterDataTO() {
        List<MasterDataTO> masterDataTOES = new ArrayList<>();
        MasterDataTO masterDataTO = new MasterDataTO();
        masterDataTO.setId(ID.longValue());
        masterDataTO.setName(NAME);
        masterDataTOES.add(masterDataTO);
        return masterDataTOES;
    }

    public static List<UniversityMasterResponseTo> getListOfUniversityMasterResponseTo() {
        List<UniversityMasterResponseTo> universityMasterResponseTos = new ArrayList<>();
        UniversityMasterResponseTo universityMasterResponseTo = new UniversityMasterResponseTo();
        universityMasterResponseTo.setId(ID);
        universityMasterResponseTo.setName(NAME);
        universityMasterResponseTos.add(universityMasterResponseTo);
        return universityMasterResponseTos;
    }

    public static List<UniversityMasterTo> getListOfUniversityMasterTo() {
        List<UniversityMasterTo> universityMasterTos = new ArrayList<>();
        UniversityMasterTo universityMasterTo = new UniversityMasterTo();
        universityMasterTo.setId(ID);
        universityMasterTo.setName(NAME);
        universityMasterTos.add(universityMasterTo);
        return universityMasterTos;
    }

    public static List<CollegeMasterResponseTo> getListOfCollegeMasterResponseTo() {
        List<CollegeMasterResponseTo> collegeMasterResponseTos = new ArrayList<>();
        CollegeMasterResponseTo collegeMasterResponseTo = new CollegeMasterResponseTo();
        collegeMasterResponseTo.setId(ID);
        collegeMasterResponseTo.setName(NAME);
        collegeMasterResponseTos.add(collegeMasterResponseTo);
        return collegeMasterResponseTos;
    }

    public static List<CollegeMasterTo> getListOfCollegeMasterTo() {
        List<CollegeMasterTo> collegeMasterResponseTos = new ArrayList<>();
        CollegeMasterTo collegeMasterResponseTo = new CollegeMasterTo();
        collegeMasterResponseTo.setId(ID);
        collegeMasterResponseTo.setName(NAME);
        collegeMasterResponseTos.add(collegeMasterResponseTo);
        return collegeMasterResponseTos;
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

    public static Address getKYCAddress() {
        Address address = new Address();
        address.setId(ID);
        address.setAddressLine1(ADDRESS_LINE_1);
        AddressType addressType = new AddressType();
        addressType.setId(in.gov.abdm.nmr.enums.AddressType.KYC.getId());
        addressType.setType(in.gov.abdm.nmr.enums.AddressType.KYC.getDescription());
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
        workFlow.setRequestId(REQUEST_ID);
        workFlow.setApplicationType(new ApplicationType(ID, HP_NAME, "desc", ""));
        workFlow.setWorkFlowStatus(new WorkFlowStatus(WorkflowStatus.APPROVED.getId(), WorkflowStatus.APPROVED.getDescription()));
        workFlow.setPreviousGroup(getUserGroup(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        return workFlow;
    }
    public WorkFlow getWorkFlowWherePreviousGroupNmc() {
        WorkFlow workFlow = new WorkFlow();
        workFlow.setId(ID);
        workFlow.setRequestId(REQUEST_ID);
        workFlow.setApplicationType(new ApplicationType(ID, HP_NAME, "desc", ""));
        workFlow.setWorkFlowStatus(new WorkFlowStatus(WorkflowStatus.APPROVED.getId(), WorkflowStatus.APPROVED.getDescription()));
        workFlow.setPreviousGroup(getUserGroup(Group.NMC.getId()));
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
        universityMaster.setCollegeId(BigInteger.TWO);
        return universityMaster;
    }

    public static UniversityMasterTo getUniversityMasterTo() {
        UniversityMasterTo universityMasterTo = new UniversityMasterTo();
        universityMasterTo.setId(ID);
        return universityMasterTo;
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
        newPassword.setPassword(CommonTestData.TEST_PSWD);
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

    public PersonalDetailsTO getPersonalDetails() {
        PersonalDetailsTO personalDetails = new PersonalDetailsTO();
        personalDetails.setSalutation(SALUTATION_DR);
        personalDetails.setAadhaarToken(TRANSACTION_ID);
        personalDetails.setFirstName(FIRST_NAME);
        personalDetails.setMiddleName(MIDDLE_NAME);
        personalDetails.setLastName(LAST_NAME);
        personalDetails.setFatherName(MIDDLE_NAME);
        personalDetails.setMotherName(MIDDLE_NAME);
        personalDetails.setSpouseName(FIRST_NAME);
        personalDetails.setCountryNationality(NationalityTO.builder().id(COUNTRY_ID).name(COUNTRY_NAME).build());
        personalDetails.setDateOfBirth(DATE_OF_BIRTH);
        personalDetails.setGender(GENDER);
        personalDetails.setSchedule(ScheduleTO.builder().id(SCHEDULE_ID).name(SCHEDULE_NAME).build());
        personalDetails.setProfilePhoto(PROFILE_PHOTO);
        personalDetails.setFullName(PROFILE_DISPLAY_NAME);
        personalDetails.setIsNew(true);
        personalDetails.setEmail(EMAIL_ID);
        personalDetails.setMobile(MOBILE_NUMBER);
        return personalDetails;
    }

    public CommunicationAddressTO getCommunicationAddressTo() {
        CommunicationAddressTO communicationAddress = new CommunicationAddressTO();
        communicationAddress.setId(ID);
        communicationAddress.setCountry(new CountryTO(COUNTRY_ID, COUNTRY_NAME, LOCALITY));
        communicationAddress.setState(new StateTO(STATE_ID, STATE_NAME, STATE_CODE));
        communicationAddress.setDistrict(new DistrictTO(DISTRICT_ID, DISTRICT_NAME, DISTRICT_CODE));
        communicationAddress.setSubDistrict(new SubDistrictTO(ID, SUB_DISTRICT_CODE, SUB_DISTRICT_NAME));
        communicationAddress.setVillage(new VillagesTO(VILLAGE_ID, VILLAGE_NAME));
        communicationAddress.setPincode(PIN_CODE);
        communicationAddress.setAddressLine1(ADDRESS_LINE_1);
        communicationAddress.setHouse(HOUSE);
        communicationAddress.setStreet(STREET);
        communicationAddress.setLocality(LOCALITY);
        communicationAddress.setLandmark(LANDMARK);
        communicationAddress.setIsSameAddress("true");
        communicationAddress.setEmail(EMAIL_ID);
        communicationAddress.setMobile(MOBILE_NUMBER);
        communicationAddress.setAddressType(new AddressTypeTO());
        communicationAddress.setCreatedAt(CURRENT_DATE);
        communicationAddress.setUpdatedAt(CURRENT_DATE);
        communicationAddress.setFullName(FIRST_NAME);
        return communicationAddress;
    }

    public HpPersonalUpdateRequestTO getHpPersonalUpdateRequestTo() {
        HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO = new HpPersonalUpdateRequestTO();
        hpPersonalUpdateRequestTO.setRequestId(REQUEST_ID);
        hpPersonalUpdateRequestTO.setPersonalDetails(getPersonalDetails());
        hpPersonalUpdateRequestTO.setCommunicationAddress(getCommunicationAddressTo());
        return hpPersonalUpdateRequestTO;
    }

    public static StateMedicalCouncilTO getStateMedicalCouncilTO() {
        StateMedicalCouncilTO stateMedicalCouncil = new StateMedicalCouncilTO();
        stateMedicalCouncil.setId(ID);
        stateMedicalCouncil.setCode(ISO_CODE);
        stateMedicalCouncil.setName(STATE_MEDICAL_COUNCIL);
        return stateMedicalCouncil;
    }


    public static RegistrationDetailTO getRegistrationDetails() {
        RegistrationDetailTO registrationDetailTO = new RegistrationDetailTO();
        registrationDetailTO.setRegistrationNumber(REGISTRATION_NUMBER);
        registrationDetailTO.setRegistrationCertificate(REGISTRATION_CERTIFICATE);
        registrationDetailTO.setFileName(FILE_NAME);
        registrationDetailTO.setFileType(FILE_TYPE);
        registrationDetailTO.setRegistrationDate(REGISTRATION_DATE);
        registrationDetailTO.setStateMedicalCouncil(getStateMedicalCouncilTO());
        return registrationDetailTO;
    }

    public static HpNbeDetails getHPNbeDetails() {
        HpNbeDetails hpNbeDetails = new HpNbeDetails();
        hpNbeDetails.setId(ID);
        hpNbeDetails.setHpProfile(getHpProfile());
        hpNbeDetails.setRollNo(ROLL_NO);
        hpNbeDetails.setMarksObtained(200);
        hpNbeDetails.setPassportNumber(PASSPORT_NO);
        hpNbeDetails.setRequestId(REQUEST_ID);
        hpNbeDetails.setMonthOfPassing(MONTH_OF_PASSING);
        hpNbeDetails.setYearOfPassing(YEAR_OF_PASSING);
        hpNbeDetails.setUserResult(RESULT);
        return hpNbeDetails;
    }

    public static CollegeTO getCollegeTo() {
        CollegeTO collegeTO = new CollegeTO();
        collegeTO.setId(ID);
        collegeTO.setName(NAME);
        return collegeTO;
    }

    public static List<QualificationDetailRequestTO> getQualification() {
        List<QualificationDetailRequestTO> qualificationDetailRequestTOList = new ArrayList<>();
        QualificationDetailRequestTO qualificationDetailRequestTO = new QualificationDetailRequestTO();
        qualificationDetailRequestTO.setRequestId(REQUEST_ID);
        qualificationDetailRequestTO.setQualificationFrom(QUALIFICATION_FROM);
        qualificationDetailRequestTO.setCountry(getCountryTO());
        qualificationDetailRequestTO.setCourse(getCourseTo());
        qualificationDetailRequestTO.setState(getStateTO());
        qualificationDetailRequestTO.setCollege(getCollegeTo());
        qualificationDetailRequestTO.setQualificationMonth(MONTH_OF_PASSING);
        qualificationDetailRequestTO.setQualificationYear(YEAR_OF_PASSING);
        qualificationDetailRequestTO.setFileName(FILE_NAME);
        qualificationDetailRequestTOList.add(qualificationDetailRequestTO);
        return qualificationDetailRequestTOList;

    }

    public HpRegistrationUpdateRequestTO getHpRegistrationUpdateRequestTo() {
        HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO = new HpRegistrationUpdateRequestTO();
        hpRegistrationUpdateRequestTO.setRegistrationDetail(getRegistrationDetails());
        hpRegistrationUpdateRequestTO.setHpNbeDetails(getHPNbeDetails());
        hpRegistrationUpdateRequestTO.setQualificationDetails(getQualification());
        return hpRegistrationUpdateRequestTO;
    }

    public static RequestCounter getRequestCounter() {
        RequestCounter requestCounter = new RequestCounter();
        ApplicationType applicationType = new ApplicationType();
        applicationType.setId(ID);
        applicationType.setName("");
        applicationType.setDescription("");
        applicationType.setRequestPrefixId(REQUEST_ID);
        requestCounter.setApplicationType(applicationType);
        requestCounter.setId(ID);
        requestCounter.setCounter(ID);

        return requestCounter;
    }

    public static List<QualificationDetailsMaster> getQualificationDetailsMasters() {
        List<QualificationDetailsMaster> qualificationDetailsMasters = new ArrayList<>();
        QualificationDetailsMaster qualificationDetailsMaster = new QualificationDetailsMaster();
        qualificationDetailsMaster.setCreatedAt(CURRENT_TIMESTAMP);
        qualificationDetailsMaster.setUniversity(getUniversityMaster());
        qualificationDetailsMasters.add(qualificationDetailsMaster);
        return qualificationDetailsMasters;
    }

    public static List<ForeignQualificationDetailsMaster> getForeignQualificationDetailsMasters() {
        List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasters = new ArrayList<>();
        ForeignQualificationDetailsMaster foreignQualificationDetailsMaster = new ForeignQualificationDetailsMaster();
        foreignQualificationDetailsMaster.setCreatedAt(CURRENT_TIMESTAMP);
        foreignQualificationDetailsMasters.add(foreignQualificationDetailsMaster);
        return foreignQualificationDetailsMasters;
    }

    public static Otp getOtp() {
        Otp otp = new Otp();
        otp.setId("1");
        otp.setAttempts(1);
        otp.setExpired(false);
        otp.setOtp(OTP);
        return otp;
    }

    public static Otp getOtpMaxAttempts() {
        Otp otp = new Otp();
        otp.setId("1");
        otp.setAttempts(OTP_MAX_ATTEMPTS);
        otp.setExpired(false);
        otp.setOtp(OTP);
        return otp;
    }

    public static List<Otp> geOtpList() {
        List<Otp> list = new ArrayList<>();
        list.add(getOtp());
        return list;
    }

    public static OtpGenerateRequestTo otpGenerateRequest() {
        OtpGenerateRequestTo otpGenerateRequestTo = new OtpGenerateRequestTo();
        otpGenerateRequestTo.setType(NotificationType.NMR_ID.getNotificationType());
        otpGenerateRequestTo.setContact(MOBILE_NUMBER);
        return otpGenerateRequestTo;
    }

    public static OtpValidateRequestTo getOtpValidateRequest() {
        OtpValidateRequestTo otpValidateRequestTo = new OtpValidateRequestTo();
        otpValidateRequestTo.setTransactionId(TRANSACTION_ID);
        otpValidateRequestTo.setContact(MOBILE_NUMBER);
        otpValidateRequestTo.setOtp(OTP);
        otpValidateRequestTo.setType(NotificationType.SMS.getNotificationType());
        return otpValidateRequestTo;
    }

    public static UserProfileTO getUserProfile() {
        UserProfileTO userProfileTO = new UserProfileTO();
        userProfileTO.setSmcId(SMC_ID);
        userProfileTO.setName(SMC_NAME);
        userProfileTO.setTypeId(UserTypeEnum.NMC.getId());
        userProfileTO.setSubTypeId(UserSubTypeEnum.NMC_ADMIN.getId());
        userProfileTO.setEmailId(CommonTestData.EMAIL_ID);
        userProfileTO.setMobileNumber(MOBILE_NUMBER);
        return userProfileTO;
    }

    public static SMCProfile getSmcProfile() {
        SMCProfile smcProfile = new SMCProfile();
        smcProfile.setId(ID);
        smcProfile.setStateMedicalCouncil(getStateMedicalCouncil());
        return smcProfile;
    }

    public static NbeProfile getNbeProfile() {
        NbeProfile nbeProfile = new NbeProfile();
        nbeProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        nbeProfile.setId(ID);
        nbeProfile.setFirstName(FIRST_NAME);
        nbeProfile.setLastName(LAST_NAME);
        nbeProfile.setMobileNo(MOBILE_NUMBER);
        nbeProfile.setUser(getUser(UserTypeEnum.NBE.getId()));
        nbeProfile.setEmailId(EMAIL_ID);
        return nbeProfile;
    }


    public static HpSmcDetailTO getHpSmcDetail() {
        HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();
        hpSmcDetailTO.setHpName(HP_NAME);
        hpSmcDetailTO.setRegistrationNumber(REGISTRATION_NUMBER);
        hpSmcDetailTO.setCouncilName(STATE_MEDICAL_COUNCIL);
        hpSmcDetailTO.setHpProfileId(HP_ID);
        hpSmcDetailTO.setEmailId(EMAIL_ID);
        hpSmcDetailTO.setAlreadyRegisteredInNmr(true);
        return hpSmcDetailTO;
    }

    public static SmcRegistrationDetailResponseTO getSmcRegistrationDetailResponse() {
        SmcRegistrationDetailResponseTO detailResponseTO = new SmcRegistrationDetailResponseTO();
        detailResponseTO.setHpName(HP_NAME);
        detailResponseTO.setRegistrationNumber(REGISTRATION_NUMBER);
        detailResponseTO.setCouncilName(STATE_MEDICAL_COUNCIL);
        detailResponseTO.setHpProfileId(HP_ID);
        detailResponseTO.setEmailId(EMAIL_ID);
        detailResponseTO.setAlreadyRegisteredInNmr(true);
        return detailResponseTO;
    }

    public static HpProfilePictureResponseTO getHpProfilePictureResponse() {
        HpProfilePictureResponseTO hpProfilePictureResponseTO = new HpProfilePictureResponseTO();
        hpProfilePictureResponseTO.setStatus(1);
        return hpProfilePictureResponseTO;
    }

    public static HpWorkProfileUpdateRequestTO getHpWorkProfileUpdateRequest() {
        HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO = new HpWorkProfileUpdateRequestTO();
        hpWorkProfileUpdateRequestTO.setCurrentWorkDetails(List.of(getCurrentWorkDetails()));
        return hpWorkProfileUpdateRequestTO;
    }

    public static CurrentWorkDetailsTO getCurrentWorkDetails() {
        CurrentWorkDetailsTO currentWorkDetails = new CurrentWorkDetailsTO();
        currentWorkDetails.setRegistrationNo(REGISTRATION_NUMBER);
        return currentWorkDetails;
    }

    public static HpProfile getHpProfileForNMR() {
        HpProfile hpProfile = new HpProfile();
        hpProfile.setNmrId(NMR_ID);
        return hpProfile;
    }

    public static UserKycTo getUserKyc() {
        UserKycTo userKycTo = new UserKycTo();
        userKycTo.setName(FIRST_NAME);
        userKycTo.setGender(GENDER);
        userKycTo.setBirthDate(DATE_OF_BIRTH);
        return userKycTo;
    }


    public static HealthProfessionalPersonalRequestTo getHealthProfessionalPersonalRequest() {
        HealthProfessionalPersonalRequestTo healthProfessionalPersonalRequestTo = new HealthProfessionalPersonalRequestTo();
        healthProfessionalPersonalRequestTo.setTransactionId(TRANSACTION_ID);
        healthProfessionalPersonalRequestTo.setMobileNumber(MOBILE_NUMBER);
        healthProfessionalPersonalRequestTo.setESignTransactionId(TRANSACTION_ID);
        return healthProfessionalPersonalRequestTo;
    }

    public static CollegeProfile getCollegeProfile() {
        CollegeProfile collegeProfile = new CollegeProfile();
        collegeProfile.setId(ID);
        collegeProfile.setUser(getUser(UserTypeEnum.COLLEGE.getId()));
        collegeProfile.setName(TEST_USER);
        collegeProfile.setCollege(getCollegeMaster());
        return collegeProfile;
    }

    public static HpProfile getHpProfileAsSuspendedStatus() {
        HpProfile hpProfile = new HpProfile();
        hpProfile.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.SUSPENDED.getId()).build());
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
        hpProfile.setUser(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));

        return hpProfile;
    }

    public static HealthProfessionalApplicationTo getHealthProfessionalApplication() {
        HealthProfessionalApplicationTo healthProfessionalApplicationTo = new HealthProfessionalApplicationTo();
        healthProfessionalApplicationTo.setApplicantFullName(PROFILE_DISPLAY_NAME);
        return healthProfessionalApplicationTo;
    }

    public static HealthProfessionalApplicationResponseTo getHealthProfessionalApplicationResponse() {
        HealthProfessionalApplicationResponseTo healthProfessionalApplicationResponseTo = new HealthProfessionalApplicationResponseTo();
        healthProfessionalApplicationResponseTo.setTotalNoOfRecords(BigInteger.ONE);
        healthProfessionalApplicationResponseTo.setHealthProfessionalApplications(List.of(getHealthProfessionalApplication()));
        return healthProfessionalApplicationResponseTo;
    }

    public static RegistrationDetails getRegistrationDetail() {
        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setRegistrationNo(REGISTRATION_NUMBER);
        registrationDetails.setId(1);
        registrationDetails.setHpProfileId(getHpProfile());
        return registrationDetails;
    }


    private ApplicationType getApplicationType() {
        ApplicationType applicationType = new ApplicationType();
        applicationType.setId(in.gov.abdm.nmr.enums.ApplicationType.HP_REGISTRATION.getId());
        applicationType.setName(in.gov.abdm.nmr.enums.ApplicationType.HP_REGISTRATION.name());
        return applicationType;
    }


    public static WorkFlowAudit getWorkFlowAudit() {
        WorkFlowAudit workFlowAudit = new WorkFlowAudit();
        workFlowAudit.setId(ID);
        workFlowAudit.setRequestId(REQUEST_ID);
        workFlowAudit.setApplicationType(getApplicationType());
        workFlowAudit.setCreatedAt(CURRENT_TIMESTAMP);
        workFlowAudit.setWorkFlowStatus(new WorkFlowStatus(WorkflowStatus.APPROVED.getId(), WorkflowStatus.APPROVED.getDescription()));
        workFlowAudit.setAction(new Action(in.gov.abdm.nmr.enums.Action.SUBMIT.getId(), in.gov.abdm.nmr.enums.Action.SUBMIT.getDescription()));
        workFlowAudit.setPreviousGroup(new UserGroup(UserTypeEnum.HEALTH_PROFESSIONAL.getId(), UserTypeEnum.HEALTH_PROFESSIONAL.getName(), UserTypeEnum.HEALTH_PROFESSIONAL.getName()));
        return workFlowAudit;
    }

    public static List<WorkFlowAudit> getWorkFlowAudits() {
        List<WorkFlowAudit> workFlowAudits = new ArrayList<>();
        workFlowAudits.add(getWorkFlowAudit());
        return workFlowAudits;

    }

}
