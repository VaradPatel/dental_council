package in.gov.abdm.nmr.domain.qualification_detail;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QualificationDetail extends CommonAuditEntity {

    @Id
    private Long id;
    private Long hpProfileId;
    private String certificate;
    private Date qualificationYear;
    private Date qualificationMonth;
    private Long universityId;
    private String qualificationName;
}
