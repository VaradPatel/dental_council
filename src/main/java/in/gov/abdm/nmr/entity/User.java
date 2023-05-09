package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "user")
public class User extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    
    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;
    
    @Column(name = "nmr_id")
    private String nmrId;
    
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
    private UserGroup group;

    private boolean accountNonLocked;

    private int failedAttempt;

    private Timestamp lockTime;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "hpr_id")
    private String hprId;

    @Column(name = "hpr_id_number")
    private String hprIdNumber;

    @Column(name = "is_new")
    private boolean isNew;

    @Column(name = "is_email_verified")
    private boolean isEmailVerified;

}
