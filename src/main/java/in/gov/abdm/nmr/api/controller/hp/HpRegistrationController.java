package in.gov.abdm.nmr.api.controller.hp;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailResponseTO;

@RestController
@RequestMapping("/hp")
public class HpRegistrationController {

    private IHpRegistrationService hpService;

    public HpRegistrationController(IHpRegistrationService hpService) {
        this.hpService = hpService;
    }

    @GetMapping(path = "/hpSmcRegistrationDetail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(@RequestBody SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
        return hpService.fetchSmcRegistrationDetail(smcRegistrationDetailRequestTO);
    }
}
