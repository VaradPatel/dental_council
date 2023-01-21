package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class CurrentWorkDetailsTO {
    private Integer facility;
    private String workOrganization;
    private OrganizationTypeTO organizationType;
    private String url;
    private AddressTO address;

}
