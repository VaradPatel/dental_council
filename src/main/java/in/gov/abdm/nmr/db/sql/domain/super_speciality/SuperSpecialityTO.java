package in.gov.abdm.nmr.db.sql.domain.super_speciality;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SuperSpecialityTO {

    private BigInteger id;
    private BigInteger hpProfileId;
    private String name;
}
