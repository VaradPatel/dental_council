package in.gov.abdm.nmr.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class User extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "user_name")
    private String username;
    private String password;
    private String refreshTokenId;
    private boolean isSmsNotificationEnabled;
    private boolean isEmailNotificationEnabled;
    @ManyToOne
    @JoinColumn(name = USER_TYPE_ID,referencedColumnName = ID)
    private UserType userType;

    @ManyToOne
    @JoinColumn(name = USER_SUB_TYPE_ID)
    private UserSubType userSubType;

    @OneToOne
    @JoinColumn(name = GROUP_ID_COLUMN,referencedColumnName = ID)
    private Group group;

    private boolean accountNonLocked;

    private int failedAttempt;

    private Timestamp lockTime;
}
