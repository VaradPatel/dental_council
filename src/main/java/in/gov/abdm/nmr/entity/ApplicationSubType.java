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
public class ApplicationSubType extends CommonAuditEntity{
    @Id
    private BigInteger id;
    @ManyToOne
    @JoinColumn(name="application_type_id")
    private ApplicationType applicationTypeId;
    private String name;
}
