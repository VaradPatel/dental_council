package in.gov.abdm.nmr.domain.hp_profile;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HpProfile extends CommonAuditEntity {

	@Id
	private Long id;
	private String aadharNumber;
	private String categoryName;
	private String changedName;
	private String createdBy;
	private Date dateOfBirth;
	private String email;
	private String emptyMandatoryFields;
	private String fatherName;
	private String firstName;
	private String fullName;
	private String gender;
	private String identifiedStateName;
	private Boolean is_manually;
	private String lastName;
	private String maritalStatus;
	private String middleName;
	private String mobileNumber;
	private String motherName;
	private String nationality;
	private BigInteger nmrId;
	private String official_telephone;
	private String other_category;
	private String panNumber;
	private String picName;
	private String primaryContactNo;
	private String professionalType;
	private String profilePhoto;
	private String salutation;
	private String signature;
	private String spouseName;
	private String systemOfMedicine;
	private String updatedBy;
	private Integer workExperienceInYear;
	private Integer yearOfInfo;
	
	
	
//	private List<String> languages;
//	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "address")
//	private Address address;

}
