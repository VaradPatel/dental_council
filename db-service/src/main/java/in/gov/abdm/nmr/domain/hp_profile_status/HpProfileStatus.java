package in.gov.abdm.nmr.domain.hp_profile_status;

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
public class HpProfileStatus extends AuditEntity {

    @Id
    private Long id;
    private String nmrId;
    private String status;
    private Long verifiedBy;
}
