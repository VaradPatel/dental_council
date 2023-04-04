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
public class Certificate extends CommonAuditEntity {

    @Id
    private Long id;
    private String url;
    private String name;
}
