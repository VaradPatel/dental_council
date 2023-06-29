package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.OptionalName;
import in.gov.abdm.validator.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VillagesTO {

    private BigInteger id;
    @District
    private String name;
}
