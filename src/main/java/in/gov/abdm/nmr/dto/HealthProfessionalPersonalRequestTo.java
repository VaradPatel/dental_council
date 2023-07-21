package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthProfessionalPersonalRequestTo {

    private String mobileNumber;

    private String transactionId;
    @JsonProperty("e_sign_transaction_id")
    private String eSignTransactionId;
}
