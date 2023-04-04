package in.gov.abdm.nmr.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.ID;

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
