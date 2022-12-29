package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.dto.AddressTO;
import in.gov.abdm.nmr.dto.OrganizationTypeTO;
import lombok.Data;

@Data
public class CurrentWorkDetailsTO {
    private Integer facility;
    private String workOrganization;
    private OrganizationTypeTO organizationType;
    private String url;
    private AddressTO address;

}
