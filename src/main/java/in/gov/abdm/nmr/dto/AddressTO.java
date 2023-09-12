package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.Address;
import in.gov.abdm.nmr.annotation.PinCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressTO {

    private BigInteger id;
    private CountryTO country;
    private StateTO state;
    private DistrictTO district;
    private VillagesTO village;
    private SubDistrictTO subDistrict;
    @PinCode
    private String pincode;
    @Address
    private String addressLine1;
    private String email;
    private String mobile;
    private AddressTypeTO addressType;
    private String createdAt;
    private String updatedAt;
    private String fullName;
    private String house;
    @Address(message = INVALID_STREET)
    private String street;
    @Address(message = INVALID_LANDMARK)
    private String landmark;
    @Address(message = INVALID_LOCALITY)
    private String locality;
    private String isSameAddress;
}
