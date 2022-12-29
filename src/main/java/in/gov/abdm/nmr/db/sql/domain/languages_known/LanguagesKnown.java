package in.gov.abdm.nmr.db.sql.domain.languages_known;

import static in.gov.abdm.nmr.api.constant.NMRConstants.ID;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.country.Country;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.db.sql.domain.language.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LanguagesKnown extends CommonAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@OneToOne
	@JoinColumn(name = "hpProfileId", referencedColumnName = ID)
	private HpProfile hpProfile;
	
	private BigInteger languageId;

}
