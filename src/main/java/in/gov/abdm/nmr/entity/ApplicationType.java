package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ApplicationType extends CommonAuditEntity {

    @Id
    private BigInteger id;
    private String name;
    private String description;
    private String requestPrefixId;
}
