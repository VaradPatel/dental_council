package in.gov.abdm.nmr.db.sql.domain.address_type;

import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddressType extends CommonAuditEntity {

	@Id
	private Integer id;
	private String addressType;
}
