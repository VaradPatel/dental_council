package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.OptionalAddress;
import in.gov.abdm.validator.AddressLine;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.INVALID_PINCODE;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class CommunicationAddressTO {

    private BigInteger id;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private CountryTO country;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private StateTO state;

    @Valid
    private DistrictTO district;

    @Valid
    private VillagesTO village;

    @Valid
    private SubDistrictTO subDistrict;

    @Size(min = 6, max = 6, message = INVALID_PINCODE)
    private String pincode;

    @AddressLine
    private String addressLine1;

    @OptionalAddress(message = "Invalid house")
    private String house;

    @OptionalAddress(message = "Invalid street")
    private String street;

    @OptionalAddress(message = "Invalid locality")
    private String locality;

    @OptionalAddress(message = "Invalid landmark")
    private String landmark;

    private String isSameAddress;
    private String email;
    private String mobile;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private AddressTypeTO addressType;
    private String createdAt;
    private String updatedAt;
    private String fullName;
}
