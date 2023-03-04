package in.gov.abdm.nmr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_BLANK_ERROR_MSG;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class IMRDetailsTO {

    @NotNull(message = NOT_NULL_ERROR_MSG)
    private String registrationNumber;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String nmrId;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String yearOfInfo;
}
