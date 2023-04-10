package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.sql.Date;

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
