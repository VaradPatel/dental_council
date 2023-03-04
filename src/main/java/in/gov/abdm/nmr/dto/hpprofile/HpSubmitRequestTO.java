package in.gov.abdm.nmr.dto.hpprofile;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class HpSubmitRequestTO {

    @NotNull(message = NOT_NULL_ERROR_MSG)
    private BigInteger hpProfileId;

    @NotNull(message = NOT_NULL_ERROR_MSG)
    private BigInteger applicationTypeId;
    private String requestId;
    private String transactionId;
    private String eSignStatus;
    private Integer hprShareAcknowledgement;
}

