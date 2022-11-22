package in.gov.abdm.nmr.domain.smc_profile;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.user_detail.UserDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SMCProfile {

	@Id
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "userDetail")
	private UserDetail userDetail;
	
	private String firstName;
	private String lastName;
	private String middleName;
	private Integer councilId;
	private Integer ndhmEnrollment;
	private Integer enrolledNumber;
	private String displayName;
}
