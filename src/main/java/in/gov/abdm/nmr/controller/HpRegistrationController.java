package in.gov.abdm.nmr.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IHpRegistrationService;

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
	public HpProfileDetailResponseTO fetchHpProfileDetail(
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId) {
		return hpService.fetchHpProfileDetail(hpProfileId);
	}

	@PutMapping(path = "/hpProfileDetail/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileUpdateResponseTO updateHpProfileDetail(@RequestBody HpProfileUpdateRequestTO hpProfileUpdateRequest,
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
			throws InvalidRequestException, WorkFlowException {
		return hpService.updateHpProfileDetail(hpProfileId, hpProfileUpdateRequest);
	}

	@PutMapping(path = "health-professional/personal/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileUpdateResponseTO updateHpProfileDetail(@RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO,
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
			throws InvalidRequestException, WorkFlowException {
		return hpService.updateHpPersonalDetail(hpProfileId, hpPersonalUpdateRequestTO);
	}
	
	@PutMapping(path = "health-professional/registration/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileUpdateResponseTO updateHpProfileDetail(@RequestBody HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO,
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
			throws InvalidRequestException, WorkFlowException {
		return hpService.updateHpRegistrationDetail(hpProfileId, hpRegistrationUpdateRequestTO);
	}

	@PutMapping(path = "health-professional/work-profile/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileUpdateResponseTO updateHpProfileDetail(@RequestBody HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO,
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
			throws InvalidRequestException, WorkFlowException {
		return hpService.updateWorkProfileDetail(hpProfileId, hpWorkProfileUpdateRequestTO);
	}
	
	
	@PostMapping(path = "health-professional/personal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileUpdateResponseTO AddHpProfileDetail(@RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO			)
			throws InvalidRequestException, WorkFlowException {
		return hpService.updateHpPersonalDetail(BigInteger.ZERO, hpPersonalUpdateRequestTO);
	}
	
	@PostMapping(path = "health-professional/registration/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileUpdateResponseTO addHpProfileDetail(@RequestBody HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO,
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
			throws InvalidRequestException, WorkFlowException {
		return hpService.updateHpRegistrationDetail(hpProfileId, hpRegistrationUpdateRequestTO);
	}

	@PostMapping(path = "health-professional/work-profile/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileUpdateResponseTO addHpProfileDetail(@RequestBody HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO,
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
			throws InvalidRequestException, WorkFlowException {
		return hpService.updateWorkProfileDetail(hpProfileId, hpWorkProfileUpdateRequestTO);
	}
	
	@PostMapping(path = "/hpProfileDetail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfileAddResponseTO addHpProfileDetail(@RequestBody HpProfileAddRequestTO hpProfileAddRequest)
			throws InvalidRequestException, WorkFlowException {
		return hpService.addHpProfileDetail(hpProfileAddRequest);
	}

	@PostMapping(path = "/hpProfileDetail/profile_picture/{hp_profile_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HpProfilePictureResponseTO uploadHpProfilePhoto(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@PathVariable(name = "hp_profile_id") BigInteger hpProfileId) throws IOException {

		return hpService.uploadHpProfilePicture(file, hpProfileId);
	}

	@PostMapping(path = "/hpProfileDetail/{hp_profile_id}/qualification",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String addQualifications(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId, @RequestBody List<QualificationDetailRequestTO> qualificationDetailRequestTOs) throws WorkFlowException {
		return hpService.addQualification(hpProfileId, qualificationDetailRequestTOs);
	}

}
