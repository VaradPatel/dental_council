package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.*;

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

    @OneToOne
    @JoinColumn(name = REGISTRATION_DETAILS_ID,referencedColumnName = ID)
    private RegistrationDetails registrationDetails;
}
