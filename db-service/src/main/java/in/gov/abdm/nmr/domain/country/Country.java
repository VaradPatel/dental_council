package in.gov.abdm.nmr.domain.country;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.domain.state.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "country")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigInteger id;
	
	private String name;
	private String nationality;
	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "country", referencedColumnName = "id")
//	private List<State> state;
}
