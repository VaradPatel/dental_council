package in.gov.abdm.nmr.domain.hp_profile;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.domain.address.Address;
import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.registration_detail.RegistrationDetail;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
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
	private Boolean isManually;
	private String lastName;
	private String maritalStatus;
	private String middleName;
	private String mobileNumber;
	private String motherName;
	private String nationality;
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
	private Integer yearOfInfo;
	

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "hp_profile_id", referencedColumnName = "id")
	private List<Address> address;
	
       
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hp_profile_id", referencedColumnName = "id")
    private List<RegistrationDetail> registrationDetails;
   
   // private List<Speciality> specialities;
   // private WorkDetails work_details;
        
   //	private List<String> languages;


}
