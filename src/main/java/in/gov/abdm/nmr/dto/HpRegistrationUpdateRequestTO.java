package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.entity.HpNbeDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpRegistrationUpdateRequestTO {
	private RegistrationDetailTO registrationDetail;
	private List<QualificationDetailRequestTO> qualificationDetails;
    private HpNbeDetails hpNbeDetails;
}
