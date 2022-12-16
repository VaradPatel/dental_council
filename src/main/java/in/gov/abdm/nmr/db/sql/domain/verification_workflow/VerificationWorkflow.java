package in.gov.abdm.nmr.db.sql.domain.verification_workflow;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.action.Action;
import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.registration_detail.RegistrationDetails;
import in.gov.abdm.nmr.db.sql.domain.verifier.Verifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VerificationWorkflow extends CommonAuditEntity {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "registration")
    private RegistrationDetails registration;

    @OneToOne
    @JoinColumn(name = "verifier")
    private Verifier verifier;

    @OneToOne
    @JoinColumn(name = "action")
    private Action action;

    private Date actionExecutionDate;
    private Date startDate;
    private Date endDate;
}
