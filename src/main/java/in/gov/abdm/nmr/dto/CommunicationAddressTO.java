package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.OptionalAddress;
import in.gov.abdm.validator.AddressLine;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.*;

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

    @OptionalAddress(message = INVALID_HOUSE)
    private String house;

    @OptionalAddress(message = INVALID_STREET)
    private String street;

    @OptionalAddress(message = INVALID_LOCALITY)
    private String locality;

    @OptionalAddress(message = INVALID_LANDMARK)
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
