package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityDetailsTO {
    private BroadSpecialityTO broadSpeciality;
    private List<SuperSpecialityTO> superSpeciality;
}
