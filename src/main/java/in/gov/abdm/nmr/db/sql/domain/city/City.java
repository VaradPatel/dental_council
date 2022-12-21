package in.gov.abdm.nmr.db.sql.domain.city;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.sub_district.SubDistrict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class City extends CommonAuditEntity {
	@Id
	private BigInteger id;
	private String name;
	private String code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subdistrict")
	private SubDistrict subdistrict;

}
