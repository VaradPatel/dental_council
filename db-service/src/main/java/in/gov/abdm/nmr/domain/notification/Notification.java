package in.gov.abdm.nmr.domain.notification;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class Notification {

	@Id
	private BigInteger id;
	
	@ManyToOne
	@JoinColumn(name = "sender_user_id", referencedColumnName = "id")
	private Login login;
	
	private String subject;
	private String description;
	private String type;
	private String status;
	private Timestamp created;
	private Integer is_read;
	private String doctor_profile_description;
	
	@ManyToOne
	@JoinColumn(name = "from_user_id", referencedColumnName = "id")
	private Login from_user_id;
	
	@ManyToOne
	@JoinColumn(name = "to_user_id", referencedColumnName = "id")
	private Login to_user_id;
	
	
	private String from_role_id;
	private String to_role_id;
	private String from_state_id;
	private String to_state_id;
	private String from_system_of_medicine_id;
	private String to_system_of_medicine_id;

}
