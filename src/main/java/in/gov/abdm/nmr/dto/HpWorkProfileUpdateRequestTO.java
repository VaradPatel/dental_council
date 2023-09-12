package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpWorkProfileUpdateRequestTO {
    private WorkDetailsTO workDetails;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private List<CurrentWorkDetailsTO> currentWorkDetails;
    private String requestId;
    private String registrationNo;
    private List<BigInteger> languagesKnownIds;
}
