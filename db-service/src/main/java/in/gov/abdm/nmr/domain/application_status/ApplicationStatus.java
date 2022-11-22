package in.gov.abdm.nmr.domain.application_status;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.hp_profile.HpProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApplicationStatus extends CommonAuditEntity {

	@Id
	private BigInteger id;
	
	@OneToOne
	@JoinColumn(name = "hpProfile")
	private HpProfile hpProfile;
	
	private String applicationStatus;
	private String createdBy;
	private String updatedBy;
}
