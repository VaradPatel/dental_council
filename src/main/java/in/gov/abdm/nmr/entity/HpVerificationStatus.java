package in.gov.abdm.nmr.entity;

import static in.gov.abdm.nmr.util.NMRConstants.APPLICATION_STATUS_TYPE_ID;
import static in.gov.abdm.nmr.util.NMRConstants.HP_PROFILE_ID;
import static in.gov.abdm.nmr.util.NMRConstants.ID;
import static in.gov.abdm.nmr.util.NMRConstants.VERIFIED_BY;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
