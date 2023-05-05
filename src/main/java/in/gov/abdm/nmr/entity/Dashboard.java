package in.gov.abdm.nmr.entity;

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
public class Dashboard extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private BigInteger applicationTypeId;
    private String requestId;
    private BigInteger hpProfileId;
    private BigInteger workFlowStatusId;
    private BigInteger smcStatus;
    private BigInteger collegeStatus;
    private BigInteger nbeStatus;
    private BigInteger nmcStatus;
}
