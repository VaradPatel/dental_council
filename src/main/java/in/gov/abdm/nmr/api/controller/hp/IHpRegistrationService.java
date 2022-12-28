package in.gov.abdm.nmr.api.controller.hp;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.hp.to.HpProfileAddRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileAddResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.api.exception.InvalidRequestException;

public interface IHpRegistrationService {

    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);
    
    HpProfileDetailResponseTO fetchHpProfileDetail(BigInteger hpProfileId);
    
    HpProfileUpdateResponseTO updateHpProfileDetail(BigInteger hpProfileId, HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException;

    HpProfileAddResponseTO addHpProfileDetail(HpProfileAddRequestTO hpProfileUpdateRequest) throws InvalidRequestException;

}


