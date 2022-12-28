package in.gov.abdm.nmr.db.sql.domain.hp_verification_status;

import in.gov.abdm.nmr.db.sql.domain.application_status_type.ApplicationStatusType;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.db.sql.domain.user_detail.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

import static in.gov.abdm.nmr.api.constant.NMRConstants.*;
import static in.gov.abdm.nmr.api.constant.NMRConstants.ID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HpVerificationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = HP_PROFILE_ID,referencedColumnName = ID)
    private HpProfile hpProfile;

    @ManyToOne
    @JoinColumn(name = APPLICATION_STATUS_TYPE_ID,referencedColumnName = ID)
    private ApplicationStatusType applicationStatusType;

    @OneToOne
    @JoinColumn(name = VERIFIED_BY,referencedColumnName = ID)
    private User verifiedBy;
}
