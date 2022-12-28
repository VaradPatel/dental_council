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
     * This holds the list of count of New applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> registrationRequests;

    /**
     * This holds the list of count of Existing applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> updationRequests;

    /**
     * This holds the list of count of Suspended applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> suspensionRequests;

    /**
     * This holds the list of count of Black-listed applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> blackListRequests;

    /**
     * This holds the list of count of Voluntary Retirement applications
     * according to their status at that point in time
     */
    private List<StatusWiseCountTO> voluntaryRetirementRequests;

}
