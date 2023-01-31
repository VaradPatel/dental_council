package in.gov.abdm.nmr.dto.college;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollegeTO {

    private BigInteger id;
    private String name;
}
