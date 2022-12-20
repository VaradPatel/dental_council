package in.gov.abdm.nmr.db.sql.domain.registration_detail;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.qualification_detail.QualificationDetails;
import in.gov.abdm.nmr.db.sql.domain.state.State;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council_status.StateMedicalCouncilStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

//    @OneToOne
    private Integer councilName;
    
    private String createdBy;
    private String dueDate;
    private String fromDate;
    private String isNameChange;
    private Integer isNuid;
    private String isRenewable;
    private Integer isRenewableRegistration;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "state")
    private State state;
//    private String status;
    private String systemOfMedicine;
    private String toDate;
    private String type;
    private String updatedBy;
    private String verifiedBy;
    private String verifiedTime;
    private Integer whetherRegisteredWithOthers;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "registration_details_id", referencedColumnName = "id")
    private List<QualificationDetails> qualificationDetails;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "councilStatus", referencedColumnName = "id")
    private StateMedicalCouncilStatus councilStatus;
}
