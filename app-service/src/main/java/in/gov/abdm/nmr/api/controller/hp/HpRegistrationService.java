package in.gov.abdm.nmr.api.controller.hp;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailResponseTO;

@Service
public class HpRegistrationService implements IHpRegistrationService {

    @Override
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
        return new SmcRegistrationDetailResponseTO();
    }
}
