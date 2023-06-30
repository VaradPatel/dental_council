package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.*;
import in.gov.abdm.nmr.annotation.PinCode;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.validator.*;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewHealthPersonalRequestTO {

    @MobileNumber
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

    @Gender
    private String gender;

    @OptionalName
    private String name;
    @PinCode
    private String pincode;

    private String birthdate;
    @OptionalAddress
    private String house;
    @OptionalAddress
    private String street;
    @OptionalAddress
    private String landmark;
    @OptionalAddress
    private String locality;
    @OptionalAddress
    private String villageTownCity;

    @SubDistrict
    private String subDist;
    @District
    private String district;

    @State
    private String state;

    private String postOffice;
    @OptionalAddress
    private String address;
}
