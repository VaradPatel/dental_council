package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressTO {

    private BigInteger id;
    private CountryTO country;
    private StateTO state;
    private DistrictTO district;
    private VillagesTO village;
    private SubDistrictTO subDistrict;
    private String pincode;
    private String addressLine1;
    private String email;
    private String mobile;
    private AddressTypeTO addressType;
    private String createdAt;
    private String updatedAt;
    private String fullName;
}
