package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_BLANK_ERROR_MSG;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class CommunicationAddressTO {

    private BigInteger id;
    private CountryTO country;
    private StateTO state;
    private DistrictTO district;

    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private VillagesTO village;
    private SubDistrictTO subDistrict;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String pincode;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String addressLine1;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String email;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String mobile;
    private AddressTypeTO addressType;
    private String createdAt;
    private String updatedAt;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String fullName;
}
