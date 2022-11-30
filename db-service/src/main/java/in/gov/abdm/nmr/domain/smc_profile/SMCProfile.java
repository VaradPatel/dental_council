package in.gov.abdm.nmr.domain.smc_profile;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.state_medical_council.StateMedicalCouncil;
import in.gov.abdm.nmr.domain.user_detail.UserDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SMCProfile extends CommonAuditEntity {

	@Id
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "userDetail")
	private UserDetail userDetail;
	
	private String firstName;
	private String lastName;
	private String middleName;
	
	@OneToOne
	private StateMedicalCouncil stateMedicalCouncil;
	
	private Integer ndhmEnrollment;
	private Integer enrolledNumber;
	private String displayName;
}
