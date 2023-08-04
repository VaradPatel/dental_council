package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CertificateTO {

    @JsonProperty("fileType")
    private String fileType;
    @JsonProperty("data")
    private String data;
}
