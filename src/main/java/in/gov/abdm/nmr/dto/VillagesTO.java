package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.Village;
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
    @Village
    private String name;
}
