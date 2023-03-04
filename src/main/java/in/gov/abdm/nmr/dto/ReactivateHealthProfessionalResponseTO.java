package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * A class with response attributes for the reactivation of the Health professional profiles by NMC
 */
@Data
public class ReactivateHealthProfessionalResponseTO {
    /**
     * Total No Records that can be in the response
     */
    private BigInteger totalNoOfRecords;
    /**
     * All the suspended health professional details that are submitted for the approval of NMC.
     */
    private List<ReactivateHealthProfessionalTO> healthProfessionalDetails;

}
