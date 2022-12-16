package in.gov.abdm.nmr.db.sql.domain.hp_profile.to;

import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.broad_speciality.BroadSpecialityTO;
import in.gov.abdm.nmr.db.sql.domain.super_speciality.SuperSpecialityTO;
import lombok.Data;

@Data
public class SpecialityDetailsTO {
	private BroadSpecialityTO broadSpeciality;
	private List<SuperSpecialityTO> superSpeciality;
}
