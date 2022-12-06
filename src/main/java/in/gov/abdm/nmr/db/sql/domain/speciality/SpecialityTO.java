package in.gov.abdm.nmr.db.sql.domain.speciality;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SpecialityTO {

    private BigInteger id;
    private String name;
    private String nationality;
}
