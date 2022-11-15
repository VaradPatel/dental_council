package in.gov.abdm.nmr.domain.college_registrar;

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
public class CollegeRegistrar extends AuditEntity {

    @Id
    private Long id;
    private String registrarName;
    private String phoneNo;

    // user table link
}
