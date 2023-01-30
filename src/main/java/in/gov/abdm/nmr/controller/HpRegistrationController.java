package in.gov.abdm.nmr.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(path = "health-professional/personal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO addHealthProfessionalPersonalDetail(@RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO)
            throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateHpPersonalDetail(null, hpPersonalUpdateRequestTO);
    }

    @PutMapping(path = "health-professional/personal/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO updateHealthProfessionalPersonalDetail(@RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO,
                                                                              @PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateHpPersonalDetail(hpProfileId, hpPersonalUpdateRequestTO);
    }

    @GetMapping(path = "health-professional/personal/{hp_profile_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalPersonalDetail(hpProfileId);
    }

    @PutMapping(path = "health-professional/registration/{hp_profile_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO updateHealthProfessionalRegistrationDetail(@RequestParam("certificate") MultipartFile certificate,
                                                                                      @RequestParam("proof") MultipartFile proof,
                                                                                      @RequestPart("data") String hpRegistrationUpdateRequestString,
                                                                                      @PathVariable(name = "hp_profile_id") BigInteger hpProfileId) throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateHpRegistrationDetail(hpProfileId, hpRegistrationUpdateRequestString, certificate, proof);
    }

    @GetMapping(path = "health-professional/registration/{hp_profile_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalRegistrationDetail(hpProfileId);
    }

    @PutMapping(path = "health-professional/work-profile/{hp_profile_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO updateHealthProfessionalWorkProfileDetail(@RequestPart("data") String hpWorkProfileUpdateRequestString,
                                                                                    @PathVariable(name = "hp_profile_id") BigInteger hpProfileId,
                                                                                    @RequestParam("proof") MultipartFile proof)

            throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateWorkProfileDetail(hpProfileId, hpWorkProfileUpdateRequestString,proof);
    }

    @GetMapping(path = "health-professional/work-profile/{hp_profile_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalWorkDetail(hpProfileId);
    }

    @PostMapping(path = "/hpProfileDetail/{hp_profile_id}/qualification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addQualifications(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId, @RequestBody List<QualificationDetailRequestTO> qualificationDetailRequestTOs) throws WorkFlowException {
        return hpService.addQualification(hpProfileId, qualificationDetailRequestTOs);
    }

    @PostMapping(path = "/hpProfileDetail/profile_picture/{hp_profile_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePictureResponseTO uploadHpProfilePhoto(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @PathVariable(name = "hp_profile_id") BigInteger hpProfileId) throws IOException {

        return hpService.uploadHpProfilePicture(file, hpProfileId);
    }

    @PostMapping(path = "health-professional/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileAddResponseTO submit(@RequestBody HpSubmitRequestTO hpSubmitRequestTO)
            throws InvalidRequestException, WorkFlowException {
        return hpService.submitHpProfile(hpSubmitRequestTO);
    }
}
