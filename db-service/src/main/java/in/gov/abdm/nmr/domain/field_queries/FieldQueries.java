package in.gov.abdm.nmr.domain.field_queries;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.domain.login.Login;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FieldQueries extends CommonAuditEntity {

	@Id
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "hpProfile")
    private HpProfile hpProfile;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Login login;

	private String fieldName;
	private String queryComment;
	private String queryBy;
	private String queryStatus;
	private String fieldLabel;
	private Integer queryType;
	private String sectionName;

}
