package in.gov.abdm.nmr.db.sql.domain.action;

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
public class Action extends CommonAuditEntity {
        @Id
        private Long id;
        private String name;

}