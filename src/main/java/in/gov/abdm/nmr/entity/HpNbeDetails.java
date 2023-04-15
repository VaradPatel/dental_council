package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HpNbeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	private String rollNo;
	private String monthOfPassing;
	private String yearOfPassing;
	private String userResult;
	private Integer marksObtained;
	private String requestId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String passportNumber;
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	@OneToOne
	@JoinColumn(name = "hpProfileId", referencedColumnName = "id")
	private HpProfile hpProfile;
}
