package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.gov.abdm.nmr.annotation.ActionId;
import in.gov.abdm.nmr.annotation.ActorId;
import in.gov.abdm.nmr.annotation.ApplicationTypeId;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.sql.Timestamp;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkFlowRequestTO {

    @NotEmpty(message = NMRConstants.CONTACT_NOT_NULL)
    private String requestId;
    @ApplicationTypeId
    private BigInteger applicationTypeId;
    @ActorId
    private BigInteger actorId;
    @ActionId
    private BigInteger actionId;
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private BigInteger hpProfileId;
    private Timestamp startDate;
    private Timestamp endDate;
    private String remarks;
    @JsonIgnore
    private BigInteger applicationSubTypeId;
}
