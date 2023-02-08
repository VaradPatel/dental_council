package in.gov.abdm.nmr.dto.masterdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterDataTO {

    private Long id;
    private String isoCode;
    private String name;
}
