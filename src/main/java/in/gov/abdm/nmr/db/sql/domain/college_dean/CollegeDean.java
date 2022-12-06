package in.gov.abdm.nmr.db.sql.domain.college_dean;

import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CollegeDean extends CommonAuditEntity {

    @Id
    private String id;
    private String deanName;
    private String phoneNo;
    private String email;

    //user table link
}
