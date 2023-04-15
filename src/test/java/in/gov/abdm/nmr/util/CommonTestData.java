package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;

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
    public static final String CODE = "20";
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
        stateMedicalCouncil.setCode(CODE);
        stateMedicalCouncil.setNameShort(STATE_SHORT_NAME);
        stateMedicalCouncil.setName(STATE_NAME);
        return stateMedicalCouncil;
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
        state.setIsoCode(CODE);
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


}
