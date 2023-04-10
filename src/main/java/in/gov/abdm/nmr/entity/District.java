package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
