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
import in.gov.abdm.nmr.db.sql.domain.work_profile.WorkProfile;
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
	@JoinColumn(name = "countryId", referencedColumnName = "id")
	private Country country;

	@OneToOne
	@JoinColumn(name = "stateId", referencedColumnName = "id")
	private State state;

	@OneToOne
	@JoinColumn(name = "districtId", referencedColumnName = "id")
	private District district;

	@OneToOne
	@JoinColumn(name = "villageId", referencedColumnName = "id")
	private Villages village;

	@OneToOne
	@JoinColumn(name = "subDistrictId", referencedColumnName = "id")
	private SubDistrict subDistrict;

	private String pincode;
	private String addressLine1;
	private String email;
	private String mobile;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "addressTypeId", referencedColumnName = "id")
	private AddressType addressTypeId;

//    @ManyToOne
//    @JoinColumn(name = "hpProfile")
//    private HpProfile hpProfile;

	private BigInteger hpProfileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workProfileId", referencedColumnName = "id")
    private WorkProfile workProfile;

//	private workProfile;
}
