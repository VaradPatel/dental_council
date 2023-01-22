package in.gov.abdm.nmr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressTypeTO {

    private Integer id;
    private String name;
}
