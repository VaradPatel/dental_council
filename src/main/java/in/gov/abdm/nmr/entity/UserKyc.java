package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserKyc extends CommonAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigInteger id;
	private BigInteger hpProfileId;
	private String txnId;
	private String mobileNumber;
	private String photo;
	private String gender;
	private String name;
	private String email;
	private String pincode;
	private Date birthDate;
	private String careOf;
	private String house;
	private String street;
	private String landmark;
	private String locality;
	private String villageTownCity;
	private String subDist;
	private String district;
	private String state;
	private String postOffice;
	private String address;
	private String registrationNo;
}
