package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HpProfilePersonalResponseTO {
    private PersonalDetailsTO personalDetails;
    private AddressTO communicationAddress;
    private AddressTO kycAddress;
    private BigInteger hpProfileId;
    private String requestId;
    private BigInteger applicationTypeId;
    private String nmrId;
    private BigInteger hpProfileStatusId;
    private BigInteger workFlowStatusId;
    private boolean isEmailVerified;
    private boolean isSmsNotificationEnabled;
    private boolean isEmailNotificationEnabled;
    private Integer esignStatus;
    private Boolean isTrackApplicationReadStatus;
    private Integer hprConsentStatus;


}
