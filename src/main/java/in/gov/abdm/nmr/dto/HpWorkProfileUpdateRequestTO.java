package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpWorkProfileUpdateRequestTO {
    private SpecialityDetailsTO specialityDetails;
    private WorkDetailsTO workDetails;
    private List<CurrentWorkDetailsTO> currentWorkDetails;
    private String requestId;
    private String registrationNo;
}
