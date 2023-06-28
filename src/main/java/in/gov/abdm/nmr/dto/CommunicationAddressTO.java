package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.Name;
import in.gov.abdm.validator.MobileNumber;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
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

    private DistrictTO district;

    private VillagesTO village;

    private SubDistrictTO subDistrict;

    @Size(min = 6, max = 6, message = INVALID_PINCODE)
    private String pincode;

    private String addressLine1;

    private String house;

    private String street;

    private String locality;

    private String landmark;

    private String isSameAddress;

    @Email
    private String email;
    @MobileNumber
    private String mobile;
    private AddressTypeTO addressType;
    private String createdAt;
    private String updatedAt;
    private String fullName;
}
