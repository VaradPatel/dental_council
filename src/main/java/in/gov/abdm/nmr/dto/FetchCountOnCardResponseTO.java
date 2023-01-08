package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Final Response Structure of fetchCountOnCard API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchCountOnCardResponseTO {

    /**
     * This holds the list of count of New applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> hpRegistrationRequests;

    /**
     * This holds the list of count of Existing applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> hpModificationRequests;

    /**
     * This holds the list of count of Temporary Suspension applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> temporarySuspensionRequests;

    /**
     * This holds the list of count of Permanent Suspension applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> permanentSuspensionRequests;

    /**
     * This holds the list of count of Activate License applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> activateLicenseRequests;

    /**
     * This holds the list of count of College Registration applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> collegeRegistrationRequests;


}
