package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.OptionalName;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
@Builder
public class NationalityTO {


    @NotNull(message = NOT_NULL_ERROR_MSG)
    private BigInteger id;

    @OptionalName(message =  "Invalid country")
    private String name;
}
