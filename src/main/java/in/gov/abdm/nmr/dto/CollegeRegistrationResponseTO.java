package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;
/**
 * A class with response attributes for the College registration in NMC
 */
@Data
public class CollegeRegistrationResponseTO {
    /**
     * Total No Records that can be in the response
     */
    private BigInteger totalNoOfRecords;
    /**
     * All the college details that are submitted for the approval of NMC.
     */
    private List<CollegeRegistrationTO> collegeDetails;
}
