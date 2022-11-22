package in.gov.abdm.nmr.domain.registration_detail;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.domain.state.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegistrationDetail extends CommonAuditEntity {

    @Id
    private Long id;
    private String certificate;
    private String councilName;
    private String councilStatus;
    private String createdBy;
    private Date dueDate;
    private Date fromDate;
    private Integer isNameChange;
    private Integer isNuid;
    private Integer isRenewable;
    private Integer isRenewableRegistration;
    private String nameChangeProofAttachment;
    private String nuidNumber;
    private Date nuidValidTill;
    private String parentCouncil;
    private String reciprocalRegistrationDone;
    private String registeredAs;
    private Date registrationDate;
    private String registrationNo;
    private Date renewableRegistrationDate;
    private Date renewableRegistrationFromDate;
    private Date renewableRegistrationToDate;
    
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "state")
    private State state;
  
    private String status;
    private String systemOfMedicine;
    private Date toDate;
    private String type;
    private String updatedBy;
    private String verifyBy;
    private Timestamp verifiedTime;
    private Integer whetherRegisteredWithOthers;

	@OneToOne
	@JoinColumn(name = "hpProfile")
    private HpProfile hpProfile;
       
}
