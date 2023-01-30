package in.gov.abdm.nmr.dto;

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
public class StateTO {

    private BigInteger id;
    private String name;
    private List<District> districts;
}
