package in.gov.abdm.nmr.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class BroadSpeciality {

    @Id
    private BigInteger id;
    private String name;
}
