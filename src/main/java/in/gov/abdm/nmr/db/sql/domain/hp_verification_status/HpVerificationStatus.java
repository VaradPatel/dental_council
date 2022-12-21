package in.gov.abdm.nmr.db.sql.domain.hp_verification_status;

import in.gov.abdm.nmr.db.sql.domain.application_status_type.ApplicationStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HpVerificationStatus {

    @Id
    private BigInteger hpVerificationStatusId;
    private BigInteger hpProfileId;
    private Integer isNewApplication;
    @ManyToOne
    @JoinColumn(name = "application_status_id",referencedColumnName = "id")
    private ApplicationStatusType applicationStatusType;
    private BigInteger verifierDeanId;
    private BigInteger verifierRegistrarId;
    private Integer verifierSmcId;
    private BigInteger verifierNmcId;
}
