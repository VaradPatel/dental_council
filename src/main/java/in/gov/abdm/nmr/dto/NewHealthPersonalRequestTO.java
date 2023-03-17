package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class NewHealthPersonalRequestTO {
    
    private String aadhaarToken;
    private String registrationNumber;
    private String smcId;
    private String mobileNumber;
    private String photo;
    private String gender;
    private String name;
    private String email;
    private String pincode;
    private String birthdate;
    private String house;
    private String street;
    private String landmark;
    private String locality;
    private String villageTownCity;
    private String subDist;
    private String district;
    private String state;
    private String postOffice;
    private String address;
}
