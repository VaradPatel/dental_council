package in.gov.abdm.nmr.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CurrentWorkDetailsTO {
    private Integer facility;
    private String workOrganization;
    private OrganizationTypeTO organizationType;
    private String url;
    private AddressTO address;

}
