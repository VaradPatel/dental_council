package in.gov.abdm.nmr.db.sql.domain.address;

import java.math.BigInteger;

import in.gov.abdm.nmr.db.sql.domain.address_type.AddressTypeTO;
import in.gov.abdm.nmr.db.sql.domain.country.CountryTO;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictTO;
import in.gov.abdm.nmr.db.sql.domain.state.StateTO;
import in.gov.abdm.nmr.db.sql.domain.sub_district.SubDistrictTO;
import in.gov.abdm.nmr.db.sql.domain.villages.VillagesTO;
import lombok.Data;

@Data
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
