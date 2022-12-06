package in.gov.abdm.nmr.db.sql.domain.university;

import java.math.BigInteger;

import lombok.Data;

@Data
public class UniversityTO {

    private BigInteger id;
    private String name;
    private String nationality;
}
