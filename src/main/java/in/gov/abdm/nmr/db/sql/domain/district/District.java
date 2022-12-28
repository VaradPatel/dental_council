package in.gov.abdm.nmr.db.sql.domain.district;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.state.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class District extends CommonAuditEntity {

	@Id
	private BigInteger id;
	private String name;
	private String isoCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stateId", referencedColumnName = "id")
	private State state;

//	@OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
//	private List<SubDistrict> subDistricts;
}
