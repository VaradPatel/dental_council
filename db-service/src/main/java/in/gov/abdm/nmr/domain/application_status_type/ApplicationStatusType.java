package in.gov.abdm.nmr.domain.application_status_type;

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
public class ApplicationStatusType extends AuditEntity {

    @Id
    private Long id;
    private String name;
}