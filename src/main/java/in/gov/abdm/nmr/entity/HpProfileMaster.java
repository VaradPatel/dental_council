package in.gov.abdm.nmr.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hpProfileMaster")
@Builder
public class HpProfileMaster {

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
	
	//@Lob
	//@Type(type="org.hibernate.type.BinaryType")
	private String profilePhoto;
		
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
	private String requestId;
	private String isSameAddress;
	private String transactionId;
	private String registrationId;
	@Column(columnDefinition ="integer default 0")
	private Integer consent;
}
