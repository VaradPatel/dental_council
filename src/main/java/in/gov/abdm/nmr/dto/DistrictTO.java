package in.gov.abdm.nmr.dto;

import in.gov.abdm.validator.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictTO {

    private BigInteger id;
    private String isoCode;
    @District
    private String name;
}
