package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWorkDetailsTO {
    private Integer facility;
    private String workOrganization;
    private OrganizationTypeTO organizationType;
    private String url;
    private AddressTO address;
    private MultipartFile proof;

}
