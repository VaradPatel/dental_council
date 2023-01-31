package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictTO {

    private BigInteger id;
    private String isoCode;
    private String name;
}
