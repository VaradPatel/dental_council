package in.gov.abdm.nmr.db.sql.domain.registration_detail;

import java.math.BigInteger;
import java.sql.Date;

import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncil;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council_status.StateMedicalCouncilStatus;
import lombok.Data;

@Data
public class RegistrationDetailTO {

    private Date registrationDate;
    private String registrationNumber;
    private StateMedicalCouncil stateMedicalCouncil;
    private StateMedicalCouncilStatus councilStatus;
    private String isRenewable;
    private Date renewableRegistrationDate;
    private BigInteger hpProfileId;
    private String isNameChange;
}
