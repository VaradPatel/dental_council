package in.gov.abdm.nmr.domain.city;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.domain.district.District;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class City {

	@Id
	private Long id;
	private String name;
	private String enShortName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district")
	private District district;

}
