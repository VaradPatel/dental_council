package in.gov.abdm.nmr.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Value
public class SmcRegistrationDetailRequestTO {

    @NotNull(message = NOT_NULL_ERROR_MSG)
    private Integer councilId;

    @NotNull(message = NOT_NULL_ERROR_MSG)
    private Integer registrationNumber;
}
