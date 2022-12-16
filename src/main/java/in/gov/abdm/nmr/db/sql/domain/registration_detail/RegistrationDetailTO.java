package in.gov.abdm.nmr.db.sql.domain.registration_detail;

import java.math.BigInteger;
import java.sql.Date;

import lombok.Data;

@Data
public class RegistrationDetailTO {

    private Date registrationDate;
    private String registrationNumber;
    private String councilName;
    private String isRenewable;
    private Date renewableRegistrationDate;
    private BigInteger hpProfileId;
    private String isNameChange;
}
