package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardRequestTO {

    private String userType;
    private String userSubType;
    private String appStatusType;
    private String hpProfileStatus;
}
