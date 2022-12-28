package in.gov.abdm.nmr.api.controller.dashboard.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTO {

    private String userType;
    private String userSubType;
}
