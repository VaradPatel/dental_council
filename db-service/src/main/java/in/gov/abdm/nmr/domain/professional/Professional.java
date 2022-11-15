package in.gov.abdm.nmr.domain.professional;


import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.domain.common.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Professional extends AuditEntity {

    @Id
    private Integer id;
    private Long registrationNo;
}
