package in.gov.abdm.nmr.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WorkFlowAudit extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    private String requestId;

    @ManyToOne
    @JoinColumn(name = "application_type_id")
    private ApplicationType applicationType;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Group createdBy;

    @ManyToOne
    @JoinColumn(name = "previous_group_id")
    private Group previousGroup;

    @ManyToOne
    @JoinColumn(name = "current_group_id")
    private Group currentGroup;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @ManyToOne
    @JoinColumn(name = "work_flow_status_id")
    private WorkFlowStatus workFlowStatus;

    @ManyToOne
    @JoinColumn(name = "hp_profile_id")
    private HpProfile hpProfile;

}
