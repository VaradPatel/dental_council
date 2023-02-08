package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_BLANK_ERROR_MSG;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
@Builder
public class NationalityTO {


    @NotNull(message = NOT_NULL_ERROR_MSG)
    private BigInteger id;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String name;
}
