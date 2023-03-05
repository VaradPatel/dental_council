package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This temporary class is used to get the list of QualificationDetailsRequestTO as a single variable
 * to reduce the complexity in receiving the list as form data and also calling object mapper over it
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualificationRequestTO {

    private List<QualificationDetailRequestTO> qualificationDetailRequestTos;

}
