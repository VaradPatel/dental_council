package in.gov.abdm.nmr.dto;

import java.sql.Date;
import java.util.List;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.NationalityTO;
import in.gov.abdm.nmr.dto.ScheduleTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    @NotBlank(message = NOT_NULL_ERROR_MSG)
    private String requestId;
}
