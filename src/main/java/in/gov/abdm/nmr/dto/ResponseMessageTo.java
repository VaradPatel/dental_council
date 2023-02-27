package in.gov.abdm.nmr.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response TO for reset password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseMessageTo {

    String transactionId;
    String message;
}