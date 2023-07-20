package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthProfessionalPersonalRequestTo {

    @NotNull
    private String mobileNumber;
    @NotNull
    private String transactionId;
    @JsonProperty("e_sign_transaction_id")
    private String eSignTransactionId;
}
