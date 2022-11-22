package in.gov.abdm.nmr.domain.district;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.domain.city.City;
import in.gov.abdm.nmr.domain.state.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class District {
	
	@Id
	private Long id;
	private String name;
	private String enShortName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state")
	private State state;
	
	@OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
	private List<City> cities;
}
