package in.gov.abdm.nmr.entity;

import static in.gov.abdm.nmr.util.NMRConstants.HP_PROFILE_STATUS_ID;
import static in.gov.abdm.nmr.util.NMRConstants.ID;
import static in.gov.abdm.nmr.util.NMRConstants.SCHEDULE_ID;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hp_nbe_details")
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
	private BigInteger hpProfileId;
}
