package in.gov.abdm.nmr.db.sql.domain.university;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.db.sql.domain.college.College;
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
public class University {

    @Id
    private Long id;
    private String name;
    private String location;

    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private List<College> colleges;
}
