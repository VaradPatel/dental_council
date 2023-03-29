package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveUserRequestTo {
    @NotBlank(message = "transactionId cannot be NULL or Blank")
    private String transactionId;

    @NotEmpty(message = NMRConstants.CONTACT_NOT_NULL)
    private String contact;
}
