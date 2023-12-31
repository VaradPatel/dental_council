package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactivateRequestResponseTo {
    private String profileId;
    private String message;
    private boolean selfReactivation;
}
