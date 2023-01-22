package in.gov.abdm.nmr.dto;

import java.math.BigInteger;
import java.util.Date;

import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.StateMedicalCouncilStatus;
import lombok.Data;

@Data
public class RegistrationDetailTO {

    private Date registrationDate;
    private String registrationNumber;
    private StateMedicalCouncilTO stateMedicalCouncil;
    private String isRenewable;
    private Date renewableRegistrationDate;
    private String isNameChange;
}
