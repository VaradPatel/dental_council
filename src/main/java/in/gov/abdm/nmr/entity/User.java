package in.gov.abdm.nmr.entity;

import static in.gov.abdm.nmr.util.NMRConstants.ID;
import static in.gov.abdm.nmr.util.NMRConstants.USER_SUB_TYPE_ID;
import static in.gov.abdm.nmr.util.NMRConstants.USER_TYPE_ID;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
