package in.gov.abdm.nmr.db.sql.domain.sub_district;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SubDistrictTO {

    private BigInteger id;
    private String name;
    private String code;
}
