package in.gov.abdm.nmr.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class ScheduleTO {

    private BigInteger id;
    private String name;
}
