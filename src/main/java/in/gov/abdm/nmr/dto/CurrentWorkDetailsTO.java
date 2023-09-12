package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.Name;
import in.gov.abdm.nmr.annotation.NotNullBlank;
import in.gov.abdm.nmr.annotation.OptionalName;
import in.gov.abdm.nmr.annotation.URL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWorkDetailsTO {
    private String facilityId;
    private BigInteger facilityTypeId;
    @Name(message = "Please enter a valid organizationType")
    private String organizationType;
    @OptionalName(message = "Please enter a valid Organisation Name")
    private String workOrganization;
    @URL
    private String url;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private AddressTO address;
    private String registrationNo;
    private byte[] proof;
    private String systemOfMedicine;
    @OptionalName(message = "Please enter a valid department")
    private String department;
    @OptionalName(message = "Please enter a valid designation")
    private String designation;
}
