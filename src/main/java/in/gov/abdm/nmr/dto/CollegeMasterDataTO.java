package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeMasterDataTO {

    private BigInteger id;
    private String name;
}
