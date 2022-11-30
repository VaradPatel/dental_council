package in.gov.abdm.nmr.domain.district;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.domain.city.City;
import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.state.State;
import in.gov.abdm.nmr.domain.sub_district.SubDistrict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class District extends CommonAuditEntity {
	
	@Id
	private Long id;
	private String name;
	private String isoCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state")
	private State state;
	
	@OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
	private List<SubDistrict> subDistricts;
}
