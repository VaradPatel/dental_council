package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

/**
 * The {@code ApplicationDetailsTo} class represents the transfer object for application details.
 */
@Data
public class ApplicationDetailsTo {

    /**
     * The workflow status ID of Application.
     */
    private BigInteger workflowStatusId;

    /**
     * The action ID of Application.
     */
    private BigInteger actionId;

    /**
     * The group ID of Application.
     */
    private BigInteger groupId;

    /**
     * The action date of Application.
     */
    private String actionDate;

    /**
     * The remarks of Application.
     */
    private String remarks;
}
