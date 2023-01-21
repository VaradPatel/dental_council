package in.gov.abdm.nmr.dto;

import java.sql.Date;
import java.util.List;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.NationalityTO;
import in.gov.abdm.nmr.dto.ScheduleTO;
import in.gov.abdm.nmr.entity.HpNbeDetails;
import lombok.Data;

@Data
public class HpRegistrationUpdateRequestTO {
	private RegistrationDetailTO registrationDetail;
	private List<QualificationDetailRequestTO> qualificationDetail;
    private String requestId;
    private String qualificationFrom;
    private HpNbeDetails hpNbeDetails;
}
