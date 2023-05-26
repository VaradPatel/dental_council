package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class PractitionerRegistrationDetailsTO {
    @JsonProperty("category")
    private BigInteger category;
    @JsonProperty("registrationData")
    private List<PractitionerRegistrationTO> registrationData;
}
