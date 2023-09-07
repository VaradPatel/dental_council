package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.gov.abdm.nmr.annotation.ActionId;
import in.gov.abdm.nmr.annotation.Alphanumeric;
import in.gov.abdm.nmr.annotation.ApplicationTypeId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.sql.Timestamp;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class ApplicationRequestTo {

    @NotNull(message = NOT_NULL_ERROR_MSG)
    private BigInteger hpProfileId;
    @ApplicationTypeId
    private BigInteger applicationTypeId;
    @ActionId
    private BigInteger actionId;
    private Timestamp fromDate;
    private Timestamp toDate;
    @Alphanumeric(message = "Please enter a valid remark.")
    private String remarks;
    @JsonIgnore
    private BigInteger applicationSubTypeId;
}
