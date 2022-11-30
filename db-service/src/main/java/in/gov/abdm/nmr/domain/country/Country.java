package in.gov.abdm.nmr.domain.country;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "country")
public class Country extends CommonAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String nationality;
	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "country", referencedColumnName = "id")
//	private List<State> state;
}
