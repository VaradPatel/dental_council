package in.gov.abdm.nmr.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity(name = "stateMedicalCouncil")
public class StateMedicalCouncil extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String code;
    private String name;
    private String nameShort;
    private String state;
}
