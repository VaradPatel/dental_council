package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDetailTO {

    private Date registrationDate;
    private String registrationNumber;
    private StateMedicalCouncilTO stateMedicalCouncil;
    private String isRenewable;
    private Date renewableRegistrationDate;
    private String isNameChange;
    private String registrationCertificate;
}
