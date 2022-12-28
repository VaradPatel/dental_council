package in.gov.abdm.nmr.api.controller.dashboard.to;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Final Response Structure of fetchCountOnCard API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTO {

    /**
     * This holds the list of count of new applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> registrationRequests;

    /**
     * This holds the list of count of existing applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> updationRequests;

    /**
     * This holds the list of count of suspended applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> suspensionRequests;

}
