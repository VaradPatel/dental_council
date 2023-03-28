package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * This class represents a response object for application details.
 */
@Data
public class ApplicationDetailResponseTo {

    /**
     * The ID of the request.
     */
    private String requestId;

    /**
     * The type of the application.
     */
    private BigInteger applicationType;

    /**
     * The date of submission of Application.
     */
    private String submissionDate;
    /**
     * The Application Pending from days.
     */
    private Long pendency;

    /**
     * The current status of the application.
     */
    private BigInteger currentStatus;

    /**
     * The ID of the current group.
     */
    private BigInteger currentGroupId;

    /**
     * The list of application details.
     */
    private List<ApplicationDetailsTo> applicationDetails;
}
