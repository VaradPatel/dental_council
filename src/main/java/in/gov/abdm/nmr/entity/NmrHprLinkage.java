package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NmrHprLinkage extends CommonAuditEntity {

	@Id
	private BigInteger id;
	private BigInteger hpProfileId;
	private String token;
	private String hprId;
	private String consent;

}
