package in.gov.abdm.nmr.domain.nmc_council_to_state_mapping;


import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NmcCouncilToStateMapping extends CommonAuditEntity {

    @Id
    private Long id;
    private Integer code;
    private String name;
//    private String mappedWith;
}
