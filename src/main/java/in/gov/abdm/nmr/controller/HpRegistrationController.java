package in.gov.abdm.nmr.controller;

import java.math.BigInteger;

import in.gov.abdm.nmr.service.IHpRegistrationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.dto.HpProfileAddResponseTO;
import in.gov.abdm.nmr.dto.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;

@RestController
@RequestMapping("/hp")
public class HpRegistrationController {

	private IHpRegistrationService hpService;

	public HpRegistrationController(IHpRegistrationService hpService) {
		this.hpService = hpService;
	}

	@GetMapping(path = "/hpSmcRegistrationDetail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(
			@RequestBody SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
		return hpService.fetchSmcRegistrationDetail(smcRegistrationDetailRequestTO);
	}

	@GetMapping(path = "/hpProfileDetail/{hp_profile_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileDetailResponseTO fetchHpProfileDetail(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId) {
    	return hpService.fetchHpProfileDetail(hpProfileId);
    }

	@PutMapping(path = "/hpProfileDetail/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileUpdateResponseTO updateHpProfileDetail(@RequestBody HpProfileUpdateRequestTO hpProfileUpdateRequest, @PathVariable(name = "hp_profile_id") BigInteger hpProfileId) throws InvalidRequestException {
    	return hpService.updateHpProfileDetail(hpProfileId, hpProfileUpdateRequest);
    }
	
	@PostMapping(path = "/hpProfileDetail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileAddResponseTO addHpProfileDetail(@RequestBody HpProfileAddRequestTO hpProfileAddRequest) throws InvalidRequestException {
    	return hpService.addHpProfileDetail(hpProfileAddRequest);
    }

}
