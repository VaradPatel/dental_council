package in.gov.abdm.nmr.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WorkFlow extends CommonAuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String requestId;
    private Timestamp startDate;
    private Timestamp endDate;
    private String remarks;
    
    @ManyToOne
    @JoinColumn(name = "application_type_id")
    private ApplicationType applicationType;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserGroup createdBy;
    
    @ManyToOne
    @JoinColumn(name = "previous_group_id")
    private UserGroup previousGroup;
    
    @ManyToOne
    @JoinColumn(name = "current_group_id")
    private UserGroup currentGroup;
    
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;
    
    @ManyToOne
    @JoinColumn(name = "work_flow_status_id")
    private WorkFlowStatus workFlowStatus;
    
    @ManyToOne
    @JoinColumn(name = "hp_profile_id")
    private HpProfile hpProfile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
}
