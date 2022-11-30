package in.gov.abdm.nmr.api.controller.hp;

import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailResponseTO;

public interface IHpRegistrationService {

    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);
}
