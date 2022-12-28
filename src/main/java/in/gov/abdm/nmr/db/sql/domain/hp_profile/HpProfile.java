package in.gov.abdm.nmr.db.sql.domain.hp_profile;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.*;

import in.gov.abdm.nmr.db.sql.domain.hp_profile_status.HpProfileStatus;
import in.gov.abdm.nmr.db.sql.domain.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static in.gov.abdm.nmr.api.constant.NMRConstants.*;
import static in.gov.abdm.nmr.api.constant.NMRConstants.ID;

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
	private String categoryName;
	private String changedName;
	private String createdAt;
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
	private String profilePhoto;
	private String salutation;
	private String signature;
	private String spouseName;
	private String systemOfMedicine;
	private String updatedAt;
	private String updatedBy;
	private String workExperienceInYear;
	private String yearOfInfo;
	private BigInteger countryNationality;

	@ManyToOne
	@JoinColumn(name = HP_PROFILE_STATUS_ID,referencedColumnName = ID)
	private HpProfileStatus hpProfileStatus;

	@OneToOne
	@JoinColumn(name = SCHEDULE_ID,referencedColumnName = ID)
	private Schedule schedule;
}
