package in.gov.abdm.nmr.domain.college_dean;

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
public class CollegeDean extends AuditEntity {

    @Id
    private Long id;
    private String deanName;
    private String phoneNo;
    private String email;

    //user table link
}
