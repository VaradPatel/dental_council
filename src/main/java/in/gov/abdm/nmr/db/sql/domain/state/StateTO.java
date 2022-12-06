package in.gov.abdm.nmr.db.sql.domain.state;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.district.District;
import lombok.Data;

@Data
public class StateTO {

    private BigInteger id;
    private String name;
    private List<District> districts;
}
