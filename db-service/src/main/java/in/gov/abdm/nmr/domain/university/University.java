package in.gov.abdm.nmr.domain.university;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.domain.college.College;
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
public class University extends CommonAuditEntity {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private List<College> colleges;
}
