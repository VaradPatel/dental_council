package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressTypeTO {

    private Integer id;
    @AddressType
    private String name;
}
