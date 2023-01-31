package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


@Data
@NoArgsConstructor
public class FetchTrackApplicationResponseTO {

    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("applicationTypeId")
    private BigInteger applicationTypeId;
    @JsonProperty("createdAt")
    private Date createdAt;
    @JsonProperty("workFlowStatusId")
    private BigInteger workFlowStatusId;
    @JsonProperty("currentGroupId")
    private BigInteger currentGroupId;
    @JsonProperty("pendencyDays")
    private BigDecimal pendencyDays;
}
