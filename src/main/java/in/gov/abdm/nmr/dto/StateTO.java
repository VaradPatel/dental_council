package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.entity.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateTO {

    private BigInteger id;
    private String name;
    private List<District> districts;
}
