package in.gov.abdm.nmr.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

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

	@ManyToOne
	@JoinColumn(name = "language_id",referencedColumnName = "id")
	private Language language;

	@ManyToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;

}
