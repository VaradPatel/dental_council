package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddressMaster extends CommonAuditEntity {

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
	private BigInteger hpProfileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workProfileId", referencedColumnName = "id")
    private WorkProfile workProfile;

}
