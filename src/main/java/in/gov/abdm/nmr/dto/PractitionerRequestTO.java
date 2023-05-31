package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PractitionerRequestTO {

    @JsonProperty("hprToken")
    private String hprToken;

    @JsonProperty("practitioner")
    private   PractitionerTO practitioner;
}
