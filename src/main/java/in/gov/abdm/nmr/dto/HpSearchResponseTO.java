package in.gov.abdm.nmr.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpSearchResponseTO {

    private List<HpSearchResultTO> results;
    private Long count;
}
