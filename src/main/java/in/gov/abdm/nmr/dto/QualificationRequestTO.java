package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.SELECT_QUALIFICATION_DETAIL_REQUEST;

/**
 * This temporary class is used to get the list of QualificationDetailsRequestTO as a single variable
 * to reduce the complexity in receiving the list as form data and also calling object mapper over it
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualificationRequestTO {

    @Valid
    @NotEmpty(message = SELECT_QUALIFICATION_DETAIL_REQUEST)
    private List<QualificationDetailRequestTO> qualificationDetailRequestTos;

}
