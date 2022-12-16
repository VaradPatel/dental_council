package in.gov.abdm.nmr.db.sql.domain.organization_type;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class OrganizationType{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private String name;

}
