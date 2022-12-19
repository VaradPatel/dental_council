package in.gov.abdm.nmr.db.sql.domain.hp_profile.to;

import in.gov.abdm.nmr.db.sql.domain.address.AddressTO;
import in.gov.abdm.nmr.db.sql.domain.organization_type.OrganizationTypeTO;
import lombok.Data;

@Data
public class CurrentWorkDetailsTO {
    private Integer facility;
    private String workOrganization;
    private OrganizationTypeTO organizationType;
    private String url;
    private AddressTO address;

}
