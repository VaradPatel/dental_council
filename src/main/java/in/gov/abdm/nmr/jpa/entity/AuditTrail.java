package in.gov.abdm.nmr.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuditTrail extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User login;

    private String ipAddress;
    private String comment;
    private String module;
    private String action;
    private String commonId;
    private String mobileNumber;
    private String emailMobile;
    private String content;
    private String requestHeader;
    private String request;
    private String response;

}
