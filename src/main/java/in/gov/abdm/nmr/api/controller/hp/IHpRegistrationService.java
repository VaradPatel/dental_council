package in.gov.abdm.nmr.api.controller.hp;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.hp.to.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.api.exception.InvalidRequestException;

public interface IHpRegistrationService {

    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);
    
    HpProfileDetailResponseTO fetchHpProfileDetail(BigInteger hpProfileId);
    
    SmcRegistrationDetailResponseTO updateHpProfileDetail(BigInteger hpProfileId, HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException;
}
