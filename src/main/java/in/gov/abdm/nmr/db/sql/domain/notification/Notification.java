package in.gov.abdm.nmr.db.sql.domain.notification;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.user_detail.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification extends CommonAuditEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", referencedColumnName = "id")
    private User login;

    private String subject;
    private String description;
    private String type;
    private String status;
    private Timestamp created;
    private Integer isRead;
    private String doctorProfileDescription;

    @ManyToOne
    @JoinColumn(name = "from_user_id", referencedColumnName = "id")
    private User fromUserId;

    @ManyToOne
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    private User toUserId;


    private String fromRoleId;
    private String toRoleId;
    private String fromStateId;
    private String toStateId;
    private String fromSystemOfMedicineId;
    private String toSystemOfMedicineId;

}
