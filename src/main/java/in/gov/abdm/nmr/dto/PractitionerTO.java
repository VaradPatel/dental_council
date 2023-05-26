package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PractitionerTO {
    @JsonProperty("profilePhoto")
    private String  profilePhoto;
    @JsonProperty("visibleProfilePicture")
    private String visibleProfilePicture;
    @JsonProperty("healthProfessionalType")
    private String healthProfessionalType;
    @JsonProperty("officialMobile")
    private String officialMobile;
    @JsonProperty("officialEmail")
    private String officialEmail;
    @JsonProperty("personalInformation")
    private PractitionerPersonalInformationTO personalInformation;
    @JsonProperty("addressAsPerKYC")
    private String  addressAsPerKYC;
    @JsonProperty("communicationAddress")
    private PractitionerAddressTO communicationAddress;
    @JsonProperty("registrationAcademic")
    private PractitionerRegistrationDetailsTO registrationAcademic;
    @JsonProperty("specialities")
    private List<PractitionerSpecialitiesTo> specialities;
    @JsonProperty("currentWorkDetails")
    private PractitionerWorkDetails currentWorkDetails;

}
