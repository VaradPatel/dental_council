package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.entity.HpNbeDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpRegistrationUpdateRequestTO {

    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private RegistrationDetailTO registrationDetail;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private List<QualificationDetailRequestTO> qualificationDetails;

    private HpNbeDetails hpNbeDetails;
}
