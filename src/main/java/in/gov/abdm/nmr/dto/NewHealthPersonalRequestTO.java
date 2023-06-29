package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.OptionalName;
import in.gov.abdm.nmr.annotation.RegistrationNumber;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewHealthPersonalRequestTO {

    //@MobileNumber
    private String mobileNumber;

    @NotBlank(message = NMRConstants.USERNAME_NOT_NULL)
    private String username;

    @NotBlank(message = NMRConstants.PASSWORD_NOT_NULL)
    private String password;

    @RegistrationNumber
    private String registrationNumber;

    @NotBlank(message = NMRConstants.SMC_NOT_NULL)
    private String smcId;

    private String hprId;

    private String hprIdNumber;

    private boolean isNew;

    @Email
    private String email;

    private String aadhaarToken;

    private String photo;

    //@Gender
    private String gender;

    @OptionalName
    private String name;

    private String pincode;

    private String birthdate;
    private String house;

    private String street;

    private String landmark;


    private String locality;

    private String villageTownCity;

    //@District
    private String subDist;
    //@District
    private String district;

    //@State
    private String state;

    private String postOffice;
    //@AddressLine
    private String address;
}
