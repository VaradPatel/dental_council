package in.gov.abdm.nmr.domain.qualification_detail;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.college.College;
import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.country.Country;
import in.gov.abdm.nmr.domain.course.Course;
import in.gov.abdm.nmr.domain.district.District;
import in.gov.abdm.nmr.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.domain.registration_detail.RegistrationDetail;
import in.gov.abdm.nmr.domain.state.State;
import in.gov.abdm.nmr.domain.university.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QualificationDetail extends CommonAuditEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String certificate;
	private Date endDate;
	private Integer isNameChange;
	private String name;
	private String nameChangeProofAttach;
	private Date qualificationMonth;
	private Date qualificationYear;
	private Date startDate;
	private String systemOfMedicine;

	@OneToOne
	@JoinColumn(name = "district")
	private District district;

	@OneToOne
	@JoinColumn(name = "college")
	private College college;

	@OneToOne
	@JoinColumn(name = "country")
	private Country country;

	@OneToOne
    @JoinColumn(name = "course")
	private Course course;

	@OneToOne
	@JoinColumn(name = "state")
	private State state;

	@OneToOne
	@JoinColumn(name = "university")
	private University university;

	@OneToOne
	@JoinColumn(name = "registrationDetail")
	private RegistrationDetail registrationDetail;

	@ManyToOne
	@JoinColumn(name = "hpProfile")
	private HpProfile hpProfile;

	private Integer isVerified;

}
