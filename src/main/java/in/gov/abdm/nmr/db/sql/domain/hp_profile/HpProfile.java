package in.gov.abdm.nmr.db.sql.domain.hp_profile;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.country.Country;
import in.gov.abdm.nmr.db.sql.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HpProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	private String aadhaarToken;
	private BigInteger scheduleId;
	private String categoryName;
	private String changedName;
	private String createdBy;
	private Date dateOfBirth;
	private String emailId;
	private String emptyMandatoryFields;
	private String fatherName;
	private String firstName;
	private String fullName;
	private String gender;
	private String identifiedStateName;
	private Boolean isManually;
	private String lastName;
	private String maritalStatus;
	private String middleName;
	private String mobileNumber;
	private String motherName;
	private String nmrId;
	private String officialTelephone;
	private String otherCategory;
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
	private String yearOfInfo;

//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "hp_profile_id", referencedColumnName = "id")
//	private List<LanguagesKnown> address;
//
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "hp_profile_id", referencedColumnName = "id")
//	private List<RegistrationDetails> registrationDetails;
//	// private List<Speciality> specialities;
//	// private WorkDetails work_details;
//	// private List<String> languages;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "countryNationalityId", referencedColumnName = "id")
	private Country countryNationalityId;
	
	@OneToOne
    @JoinColumn(name = "user_detail_id")
    private User userDetail;
}
