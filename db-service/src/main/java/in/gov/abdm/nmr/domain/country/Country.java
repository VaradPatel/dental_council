package in.gov.abdm.nmr.domain.country;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Entity
public class Country {

	@Id
	private Long id;
	private String name;
	private String enShortName;

	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
	private List<State> states;
}
