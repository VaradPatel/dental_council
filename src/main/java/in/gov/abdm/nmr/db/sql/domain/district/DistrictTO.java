package in.gov.abdm.nmr.db.sql.domain.district;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DistrictTO {

    private BigInteger id;
    private String isoCode;
    private String name;
}
