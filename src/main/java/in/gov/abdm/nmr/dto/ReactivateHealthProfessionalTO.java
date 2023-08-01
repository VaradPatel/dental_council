package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * A class with the Health professional  details that would be shared with the NMC
 */
@Data
public class ReactivateHealthProfessionalTO {
    private BigInteger healthProfessionalId;
    private String registrationId;
    private String requestId;
    private String healthProfessionalName;
    private String submittedDate;
    private String reactivation;
    private BigInteger typeOfSuspension;
    private String remarks;
    private String gender;
    private String emailId;
    private String mobileNumber;
    private String reactivationFile;
    private String fileName;
    private String fileType;
}
