package in.gov.abdm.nmr.dto;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class NewHealthPersonalRequestTO {

    @NotBlank(message = NMRConstants.USERNAME_NOT_NULL)
    private String mobileNumber;

    @NotBlank(message = NMRConstants.USERNAME_NOT_NULL)
    private String username;

    @NotBlank(message = NMRConstants.PASSWORD_NOT_NULL)
    private String password;

    @NotBlank(message = NMRConstants.REGISTRATION_NOT_NULL)
    private String registrationNumber;

    @NotBlank(message = NMRConstants.SMC_NOT_NULL)
    private String smcId;

    private String hprId;

    private String hprIdNumber;

    private boolean isNew;

    private String email;

    private String aadhaarToken;

    private String photo;

    private String gender;

    private String name;

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
