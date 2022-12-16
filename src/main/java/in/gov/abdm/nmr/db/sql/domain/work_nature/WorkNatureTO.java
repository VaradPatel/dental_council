package in.gov.abdm.nmr.db.sql.domain.work_nature;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.district.District;
import lombok.Data;

@Data
public class WorkNatureTO {

    private BigInteger id;
    private String name;
}
