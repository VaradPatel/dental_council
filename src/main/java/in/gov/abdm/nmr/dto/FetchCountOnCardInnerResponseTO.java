package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchCountOnCardInnerResponseTO {

    private String applicationTypeIds;
    private List<StatusWiseCountTO> statusWiseCount;

}
