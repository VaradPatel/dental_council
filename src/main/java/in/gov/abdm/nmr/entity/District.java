package in.gov.abdm.nmr.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class District extends CommonAuditEntity {

    @Id
    private BigInteger id;
    private String name;
    private String isoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateId", referencedColumnName = "id")
    private State state;

}
