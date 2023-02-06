package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class HealthProfessionalApplicationResponseTo {
    private BigInteger totalNoOfRecords;
    private List<HealthProfessionalApplicationTo> healthProfessionalApplications;
}
