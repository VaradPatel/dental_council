package in.gov.abdm.nmr.db.sql.domain.work_status;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.district.District;
import lombok.Data;

@Data
public class WorkStatusTO {

    private BigInteger id;
    private String name;
}
