package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkStatusTO {

    private BigInteger id;
    private String name;
}
