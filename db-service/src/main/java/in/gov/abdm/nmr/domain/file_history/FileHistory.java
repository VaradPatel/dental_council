package in.gov.abdm.nmr.domain.file_history;

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
public class FileHistory extends AuditEntity {

    @Id
    private Long id;
    private String completedAt;
    private String fileName;
    private String location;
    private String startedAt;
    private String status;
}
