package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class PractitionerWorkDetails {
    @JsonProperty("currentlyWorking")
    private BigInteger currentlyWorking;
    @JsonProperty("purposeOfWork")
    private String purposeOfWork;
    @JsonProperty("chooseWorkStatus")
    private BigInteger chooseWorkStatus;
    @JsonProperty("reasonForNotWorking")
    private  String reasonForNotWorking;
    @JsonProperty("facilityDeclarationData")
    private  PractitionerFacilityDataTo facilityDeclarationData;
}
