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
	private String id;
	
	@OneToOne
	@JoinColumn(name = "hpProfile")
    private HpProfile hpProfile;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Login login;
	
	private String field_name;
	private String query_comment;
	private String query_by;
	private String query_status;
	private String field_label;
	private Integer query_type;
	private String section_name;

}
