package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "university")
@Table(name = "universities")
public class University {

    @Id
    private BigInteger id;
    private String name;
    private String location;
}
