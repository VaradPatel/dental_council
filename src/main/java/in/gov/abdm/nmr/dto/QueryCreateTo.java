package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * dto for queries
 */

@Data
public class QueryCreateTo {

    @JsonProperty("hpProfileId")
    private BigInteger hpProfileId;

    private List<QueryTo> queries;

    @JsonProperty("commonComment")
    private String commonComment;

    @JsonProperty("queryBy")
    private String queryBy;

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
