package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FuzzyParameter {
    String field;
    String registeredValue;
    String kycValue;
    String status;
}
