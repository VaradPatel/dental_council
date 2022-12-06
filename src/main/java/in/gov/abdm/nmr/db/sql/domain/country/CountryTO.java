package in.gov.abdm.nmr.db.sql.domain.country;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CountryTO {

    private BigInteger id;
    private String name;
    private String nationality;
}
