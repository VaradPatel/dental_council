package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class PractitionerSpecialitiesTo {
    @JsonProperty("speciality")
    private BigInteger speciality;
    @JsonProperty("subSpecialities")
    private BigInteger subSpecialities;
}
