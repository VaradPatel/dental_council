package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWorkDetailsTO {
    private BigInteger facilityId;
    private BigInteger facilityTypeId;
    private String organizationType;
    private String workOrganization;
    private String url;
    private AddressTO address;
    private MultipartFile proof;

}
