package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueryTo {

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("fieldLabel")
    private String fieldLabel;

    @JsonProperty("sectionName")
    private String sectionName;

    @JsonProperty("queryComment")
    private String queryComment;

    @JsonProperty("queryStatus")
    private String queryStatus;

}