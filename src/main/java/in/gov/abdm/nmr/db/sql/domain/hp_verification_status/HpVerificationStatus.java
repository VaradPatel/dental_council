package in.gov.abdm.nmr.db.sql.domain.hp_verification_status;

import static in.gov.abdm.nmr.api.constant.NMRConstants.APPLICATION_STATUS_TYPE_ID;
import static in.gov.abdm.nmr.api.constant.NMRConstants.HP_PROFILE_ID;
import static in.gov.abdm.nmr.api.constant.NMRConstants.ID;
import static in.gov.abdm.nmr.api.constant.NMRConstants.VERIFIED_BY;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.application_status_type.ApplicationStatusType;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.db.sql.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
