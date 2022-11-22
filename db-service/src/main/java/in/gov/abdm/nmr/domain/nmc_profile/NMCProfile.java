package in.gov.abdm.nmr.domain.nmc_profile;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.user_detail.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NMCProfile {

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
