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
public class RequestCounter extends CommonAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = "application_type_id")
    private ApplicationType applicationType;

    private BigInteger counter;
}
