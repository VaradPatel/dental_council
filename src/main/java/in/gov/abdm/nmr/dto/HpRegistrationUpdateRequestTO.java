package in.gov.abdm.nmr.dto;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.NationalityTO;
import in.gov.abdm.nmr.dto.ScheduleTO;
import in.gov.abdm.nmr.entity.HpNbeDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpRegistrationUpdateRequestTO {
	private RegistrationDetailTO registrationDetail;
	private List<QualificationDetailRequestTO> qualificationDetail;
    private HpNbeDetails hpNbeDetails;
}
