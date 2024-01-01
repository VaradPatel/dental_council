package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseTO {

    private BigInteger profileId;
    private BigInteger collegeId;
    private BigInteger userId;
    private BigInteger userType;
    private BigInteger userSubType;
    private BigInteger userGroupId;
    private Boolean hpRegistered;
    private Boolean blacklisted;
    private Integer esignStatus;
    private Timestamp lastLogin;
    private BigInteger hpProfileStatusId;
    private BigInteger workFlowStatusId;
    private Boolean isAdmin;
}
