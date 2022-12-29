package in.gov.abdm.nmr.dto;

import java.util.List;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import lombok.Data;

@Data
public class SpecialityDetailsTO {
	private BroadSpecialityTO broadSpeciality;
	private List<SuperSpecialityTO> superSpeciality;
}
