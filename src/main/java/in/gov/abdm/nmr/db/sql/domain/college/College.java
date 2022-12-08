package in.gov.abdm.nmr.db.sql.domain.college;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.university.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class College {

    @Id
    private String id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university")
    private University university;
}