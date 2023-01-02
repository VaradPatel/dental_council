package in.gov.abdm.nmr.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.entity.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Professional extends CommonAuditEntity {

    @Id
    private Long id;
    private Long registrationNo;
}