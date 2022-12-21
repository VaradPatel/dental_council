package in.gov.abdm.nmr.db.sql.domain.qualification_status;

import java.math.BigInteger;

import lombok.Data;

@Data
public class QualificationStatusTO {
    private BigInteger id;
    private String name;
}
