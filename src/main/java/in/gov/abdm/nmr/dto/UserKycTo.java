package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserKycTo {

	private BigInteger hpProfileId;
	private String txnId;
	private String mobileNumber;
	private String photo;
	private String gender;
	private String name;
	private String email;
	private String pincode;
	private String birthDate;
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
	private Timestamp createdAt;
	private Timestamp updatedAt;
}
