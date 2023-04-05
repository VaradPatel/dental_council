package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.ID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LanguagesKnownMaster extends CommonAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	
	@ManyToOne
	@JoinColumn(name = "language_id",referencedColumnName = "id")
	private Language language;

	@ManyToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;

}
