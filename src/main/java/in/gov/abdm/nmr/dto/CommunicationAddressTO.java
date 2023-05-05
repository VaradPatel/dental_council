package in.gov.abdm.nmr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.INVALID_PINCODE;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_BLANK_ERROR_MSG;

@Data
public class CommunicationAddressTO {

    private BigInteger id;
    private CountryTO country;
    private StateTO state;
    private DistrictTO district;

    private VillagesTO village;
    private SubDistrictTO subDistrict;

    @Size(min = 6, max = 6, message = INVALID_PINCODE)
    private String pincode;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String addressLine1;

    private String house;

    private String street;

    private String locality;

    private String landmark;

    private String isSameAddress;

    private String email;

    private String mobile;
    private AddressTypeTO addressType;
    private String createdAt;
    private String updatedAt;

    private String fullName;
}
