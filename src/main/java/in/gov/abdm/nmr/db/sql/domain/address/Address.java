package in.gov.abdm.nmr.db.sql.domain.address;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.address_type.AddressType;
import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.country.Country;
import in.gov.abdm.nmr.db.sql.domain.district.District;
import in.gov.abdm.nmr.db.sql.domain.state.State;
import in.gov.abdm.nmr.db.sql.domain.sub_district.SubDistrict;
import in.gov.abdm.nmr.db.sql.domain.villages.Villages;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

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
	private Villages village;

	@OneToOne
	@JoinColumn(name = "subDistrict")
	private SubDistrict subDistrict;

	private String pincode;
	private String addressLine1;
	private String email;
	private String mobile;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_type_id")
	private AddressType addressTypeId;

//    @ManyToOne
//    @JoinColumn(name = "hpProfile")
//    private HpProfile hpProfile;

	private BigInteger hpProfileId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "workProfile")
//    private WorkProfile workProfile;

	private BigInteger workProfile;
}
