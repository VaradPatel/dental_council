package in.gov.abdm.nmr.db.sql.domain.nationality;

import java.math.BigInteger;

import lombok.Data;

@Data
public class NationalityTO {
    private BigInteger id;
    private String name;
}
