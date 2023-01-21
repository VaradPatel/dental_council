package in.gov.abdm.nmr.dto;

import java.sql.Date;
import java.util.List;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.NationalityTO;
import in.gov.abdm.nmr.dto.ScheduleTO;
import lombok.Data;

@Data
public class HpPersonalUpdateRequestTO {
	private PersonalDetailsTO personalDetails;
    private CommunicationAddressTO communicationAddress;
    private IMRDetailsTO imrDetails;
    private String requestId;
}
