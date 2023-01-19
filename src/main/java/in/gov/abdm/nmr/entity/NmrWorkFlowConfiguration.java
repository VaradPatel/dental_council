package in.gov.abdm.nmr.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NmrWorkFlowConfiguration extends CommonAuditEntity {

    @Id
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "application_type_id")
    private ApplicationType applicationType;

    @ManyToOne
    @JoinColumn(name = "action_performed_by")
    private UserGroup actionPerformedBy;

    @ManyToOne
    @JoinColumn(name = "action_performed")
    private Action action;

    @ManyToOne
    @JoinColumn(name = "assign_to")
    private UserGroup assignTo;
    private Boolean isActive;
    private Timestamp activeFrom;
    private Timestamp activeUntil;

    @ManyToOne
    @JoinColumn(name="work_flow_status_id")
    private WorkFlowStatus workFlowStatus;
}
