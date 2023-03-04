package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "country")
public class Country extends CommonAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigInteger id;

	private String name;
	private String nationality;

}
