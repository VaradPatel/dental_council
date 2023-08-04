package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
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

    @NotEmpty(message = NMRConstants.QUERY_RAISED_ERROR)
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
