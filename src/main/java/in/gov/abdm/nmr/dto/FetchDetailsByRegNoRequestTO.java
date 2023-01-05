package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchDetailsByRegNoRequestTO {

    private String registrationNumber;
    private String smcName;
    private String userType;
    private String userSubType;

}
