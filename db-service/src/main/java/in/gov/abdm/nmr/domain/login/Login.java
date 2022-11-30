package in.gov.abdm.nmr.domain.login;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Login {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigInteger id;

	private String userName;
	private String password;
	private Long userType;

}
