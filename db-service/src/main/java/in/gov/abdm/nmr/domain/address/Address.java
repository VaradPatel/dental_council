package in.gov.abdm.nmr.domain.address;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.address_type.AddressType;
import in.gov.abdm.nmr.domain.city.City;
import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.country.Country;
import in.gov.abdm.nmr.domain.district.District;
import in.gov.abdm.nmr.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.domain.state.State;
import in.gov.abdm.nmr.domain.sub_district.SubDistrict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address extends CommonAuditEntity {

	@Id
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "country")
	private Country country;
	
	@OneToOne
	@JoinColumn(name = "state")
	private State state;
	
	@OneToOne
	@JoinColumn(name = "district")
	private District district;
	
	@OneToOne
	@JoinColumn(name = "city")
	private City city;
	
	@OneToOne
	@JoinColumn(name = "subDistrict")
	private SubDistrict subDistrict;
	
	private String pincode;
	private String addressLine1;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_type")
	private AddressType addressType;

	@ManyToOne
	@JoinColumn(name = "hpProfile")
	private HpProfile hpProfile;
}
