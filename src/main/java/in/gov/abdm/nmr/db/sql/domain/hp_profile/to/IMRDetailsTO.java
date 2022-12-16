package in.gov.abdm.nmr.db.sql.domain.hp_profile.to;

import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.language.LanguageTO;
import lombok.Data;

@Data
public class IMRDetailsTO {
	private String registrationNumber;
	private String nmrId;
	private String yearOfInfo;
}
