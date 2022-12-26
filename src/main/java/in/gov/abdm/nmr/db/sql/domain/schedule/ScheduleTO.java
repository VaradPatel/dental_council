package in.gov.abdm.nmr.db.sql.domain.schedule;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ScheduleTO {

    private BigInteger id;
    private String name;
}
