package in.gov.abdm.nmr.domain.audit_trail;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
public class AuditTrail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigInteger id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Login login;
	
	private String ipAddress;
	private String comment;
	private String module;
	private String action;
	private String commonId;
	private String mobileNumber;
	private String emailMobile;
	private String content;
	private String requestHeader;
	private String request;
	private String response;

}
