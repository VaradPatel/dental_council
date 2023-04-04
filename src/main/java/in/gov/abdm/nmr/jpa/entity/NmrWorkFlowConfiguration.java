package in.gov.abdm.nmr.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigInteger;
import java.sql.Timestamp;

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

    @ManyToOne
    @JoinColumn(name="application_sub_type_id")
    private ApplicationSubType applicationSubType;
}
