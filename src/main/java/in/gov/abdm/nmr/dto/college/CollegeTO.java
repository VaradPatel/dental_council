package in.gov.abdm.nmr.dto.college;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.District;
import lombok.Data;

@Data
public class CollegeTO {

    private BigInteger id;
    private String name;
    private List<District> districts;
}
