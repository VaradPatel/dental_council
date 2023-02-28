package in.gov.abdm.nmr.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class HpPersonalUpdateRequestTO {

    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private PersonalDetailsTO personalDetails;

    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private CommunicationAddressTO communicationAddress;

    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private IMRDetailsTO imrDetails;

    private String requestId;
}
