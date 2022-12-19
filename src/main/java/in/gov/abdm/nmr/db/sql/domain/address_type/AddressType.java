package in.gov.abdm.nmr.db.sql.domain.address_type;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddressType {

    @Id
    private Integer id;
    private String addressType;
}
