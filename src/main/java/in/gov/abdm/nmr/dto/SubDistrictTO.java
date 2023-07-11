package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.SubDistrict;
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
    @SubDistrict
    private String name;
    private String isoCode;
}
