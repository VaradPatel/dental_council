package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * dto for queries
 */
@Data
public class QueryCreateTo {

    @JsonProperty("hpProfileId")
    private BigInteger hpProfileId;

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("fieldLabel")
    private String fieldLabel;

    @JsonProperty("sectionName")
    private String sectionName;

    @JsonProperty("queryComment")
    private String queryComment;

    @JsonProperty("commonComment")
    private String commonComment;

    @JsonProperty("queryBy")
    private String queryBy;

    @JsonProperty("queryStatus")
    private String queryStatus;

    @JsonProperty("createdAt")
    private Timestamp createdAt;

    @JsonProperty("updatedAt")
    private Timestamp updatedAt;

    @JsonProperty("groupId")
    private BigInteger groupId;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("applicationTypeId")
    private BigInteger applicationTypeId;
}
