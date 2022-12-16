package in.gov.abdm.nmr.db.sql.domain.broad_speciality;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BroadSpeciality {

    @Id
    private Long id;
    private String name;
}
