package in.gov.abdm.nmr.dto;

import java.math.BigInteger;
import java.sql.Date;

import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.StateMedicalCouncilStatus;
import lombok.Data;

@Data
public class RegistrationDetailTO {

    private Date registrationDate;
    private String registrationNumber;
    private StateMedicalCouncil stateMedicalCouncil;
    private StateMedicalCouncilStatus councilStatus;
    private String isRenewable;
    private Date renewableRegistrationDate;
    private String isNameChange;
}
