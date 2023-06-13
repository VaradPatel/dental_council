package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWorkDetailsTO {
    private String facilityId;
    private BigInteger facilityTypeId;
    private String organizationType;
    private String workOrganization;
    private String url;
    private AddressTO address;
    private String registrationNo;
    private byte[] proof;
    private Integer experienceInYears;
    private String systemOfMedicine;
    private String department;
    private String designation;
    private String reason;
    private String remark;

}
