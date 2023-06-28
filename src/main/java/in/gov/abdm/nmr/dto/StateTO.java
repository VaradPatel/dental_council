package in.gov.abdm.nmr.dto;

import in.gov.abdm.validator.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateTO {
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private BigInteger id;
    @State
    private String name;
    private String isoCode;
}
