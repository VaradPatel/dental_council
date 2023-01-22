package in.gov.abdm.nmr.entity;

import static in.gov.abdm.nmr.util.NMRConstants.ID;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class LanguagesKnown extends CommonAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@OneToOne
	@JoinColumn(name = "hpProfileId", referencedColumnName = ID)
	private HpProfile hpProfile;
	
	private BigInteger languageId;

}
