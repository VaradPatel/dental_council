package in.gov.abdm.nmr.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response TO for reset password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycResponseMessageTo {

    List<FuzzyParameter> fuzzParameters;
    String kycFuzzyMatchStatus;
}