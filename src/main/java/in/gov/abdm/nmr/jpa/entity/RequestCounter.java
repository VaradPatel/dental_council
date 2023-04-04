package in.gov.abdm.nmr.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class RequestCounter extends CommonAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = "application_type_id")
    private ApplicationType applicationType;

    private BigInteger counter;
}
