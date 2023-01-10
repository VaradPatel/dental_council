package in.gov.abdm.nmr.entity;

import static in.gov.abdm.nmr.util.NMRConstants.HP_PROFILE_STATUS_ID;
import static in.gov.abdm.nmr.util.NMRConstants.ID;
import static in.gov.abdm.nmr.util.NMRConstants.SCHEDULE_ID;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hpProfile")
public class HpProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	private String aadhaarToken;
	private String categoryName;
	private String changedName;
	private Timestamp createdAt;
	private String createdBy;
	private Date dateOfBirth;
	private String emailId;
	private String emptyMandatoryFields;
	private String fatherName;
	private String firstName;
	private String fullName;
	private String gender;
	private String identifiedStateName;
	private String isManually;
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
	
	@Lob
	@Type(type="org.hibernate.type.BinaryType")
	private byte[] profilePhoto;
		
	private String salutation;
	private String signature;
	private String spouseName;
	private String systemOfMedicine;
	private Timestamp updatedAt;
	private String updatedBy;
	private String workExperienceInYear;
	private String yearOfInfo;
	
	@OneToOne
	@JoinColumn(name = "countryNationalityId", referencedColumnName = ID)
	private Country countryNationality;

	@ManyToOne
	@JoinColumn(name = HP_PROFILE_STATUS_ID,referencedColumnName = ID)
	private HpProfileStatus hpProfileStatus;

	@OneToOne
	@JoinColumn(name = SCHEDULE_ID,referencedColumnName = ID)
	private Schedule schedule;
	
	@OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
