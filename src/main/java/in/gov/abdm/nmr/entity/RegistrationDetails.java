package in.gov.abdm.nmr.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static in.gov.abdm.nmr.util.NMRConstants.ID;
import static in.gov.abdm.nmr.util.NMRConstants.STATE_MEDICAL_COUNCIL_ID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegistrationDetails extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String certificate;

    @OneToOne
    @JoinColumn(name = STATE_MEDICAL_COUNCIL_ID,referencedColumnName = ID)
    private StateMedicalCouncil stateMedicalCouncil;

    //    private String createdAt;
    private String createdBy;
    private String dueDate;
    private String fromDate;
    private String isNameChange;
    private String isNuid;
    private String isRenewable;
    private String isRenewableRegistration;
    private String nameChangeProofAttachment;
    private String nuidNumber;
    private String nuidValidTill;
    private String parentCouncil;
    private String reciprocalRegistrationDone;
    private String registeredAs;
    private Date registrationDate;
    private String registrationNo;
    private Date renewableRegistrationDate;
    private Date renewableRegistrationFromDate;
    private Date renewableRegistrationToDate;

    //    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "state")
    private String state;
    private String status;
    private String systemOfMedicine;
    private String toDate;
    private String type;
    private String updatedBy;
    private String verifiedBy;
    private String verifiedTime;
    private String whetherRegisteredWithOthers;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "registration_details_id", referencedColumnName = "id")
//    private List<QualificationDetails> qualificationDetails;

    //    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "councilStatus", referencedColumnName = "id")
//    private BigInteger councilStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "councilStatus", referencedColumnName = "id")
    private StateMedicalCouncilStatus councilStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hpProfileId", referencedColumnName = "id")
    private HpProfile hpProfileId;

    private String requestId;
    
}
