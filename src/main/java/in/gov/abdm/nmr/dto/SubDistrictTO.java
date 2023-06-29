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
public class SubDistrictTO {

    private BigInteger id;
    @District
    private String name;
    private String isoCode;
}
