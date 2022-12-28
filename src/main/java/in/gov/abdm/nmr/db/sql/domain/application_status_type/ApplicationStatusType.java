package in.gov.abdm.nmr.db.sql.domain.application_status_type;

import java.math.BigInteger;
import java.sql.Timestamp;

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
public class ApplicationStatusType extends CommonAuditEntity {

    @Id
    private BigInteger id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String name;
}