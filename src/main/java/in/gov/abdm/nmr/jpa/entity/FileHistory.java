package in.gov.abdm.nmr.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileHistory extends CommonAuditEntity {

    @Id
    private Long id;
    private String completedAt;
    private String fileName;
    private String location;
    private String startedAt;
    private String status;
}
