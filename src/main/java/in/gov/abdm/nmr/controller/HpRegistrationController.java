package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * The HpRegistrationController class is a RestController that handles requests for registration related operations.
 */
@RestController
@RequestMapping("/hp")
@Validated
public class HpRegistrationController {

    /**
     * The variable hpService holds an instance of IHpRegistrationService.
     */
    private IHpRegistrationService hpService;

    /**
     * Constructor for HpRegistrationController class.
     *
     * @param hpService an instance of IHpRegistrationService interface.
     */
    public HpRegistrationController(IHpRegistrationService hpService) {
        this.hpService = hpService;
    }

    /**
     * This method is used to fetch SMC registration detail information.
     *
     * @param smcRegistrationDetailRequestTO This is the request object that contains the information required to fetch the SMC registration detail.
     * @return SmcRegistrationDetailResponseTO This returns the SMC registration detail information.
     */
    @PostMapping(path = "/hpSmcRegistrationDetail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(
            @Valid @RequestBody SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
        return hpService.fetchSmcRegistrationDetail(smcRegistrationDetailRequestTO);
    }

    /**
     * Adds or updates the health professional's personal detail.
     *
     * @param hpPersonalUpdateRequestTO {@link HpPersonalUpdateRequestTO} - The request object that contains the personal details of the health professional.
     * @return {@link HpProfilePersonalResponseTO} - The response object that contains the updated personal details of the health professional.
     * @throws InvalidRequestException - Thrown if the request is invalid.
     * @throws WorkFlowException       - Thrown if there is an error in the workflow.
     */
    @PostMapping(path = "health-professional/personal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO addHealthProfessionalPersonalDetail(
            @Valid @RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO)
            throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateHpPersonalDetail(null, hpPersonalUpdateRequestTO);
    }

    /**
     * Updates the health professional's personal details.
     *
     * @param hpPersonalUpdateRequestTO contains the updated personal details.
     * @param hpProfileId               the unique identifier for the health professional's profile.
     * @return HpProfilePersonalResponseTO the updated personal details.
     * @throws InvalidRequestException when the request is invalid.
     * @throws WorkFlowException       when there's an error with the workflow.
     */
    @PutMapping(path = "health-professional/personal/{hp_profile_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO updateHealthProfessionalPersonalDetail(
            @Valid @RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO,
            @PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateHpPersonalDetail(hpProfileId, hpPersonalUpdateRequestTO);
    }

    /**
     * This method is used to get the personal details of a health professional based on the hp_profile_id.
     *
     * @param hpProfileId The unique identifier of the health professional.
     * @return An instance of {@link HpProfilePersonalResponseTO} containing the personal details of the health professional.
     * @throws InvalidRequestException If the request is invalid.
     * @throws WorkFlowException       If there is a problem with the workflow.
     */
    @GetMapping(path = "health-professional/personal/{hp_profile_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalPersonalDetail(hpProfileId);
    }

    /**
     * This method updates the health professional registration details.
     *
     * @param certificate                       The health professional's certificate file.
     * @param proof                             The health professional's proof file.
     * @param hpRegistrationUpdateRequestString The health professional registration update request in string format.
     * @param hpProfileId                       The health professional profile id.
     * @return The updated health professional registration response in a transfer object.
     * @throws InvalidRequestException If the provided request is invalid.
     * @throws WorkFlowException       If there is an error in the workflow.
     */
    @PutMapping(path = "health-professional/registration/{hp_profile_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO updateHealthProfessionalRegistrationDetail(@RequestParam("certificate") MultipartFile certificate,
                                                                                      @RequestParam("proof") MultipartFile proof,
                                                                                      @RequestPart("data") String hpRegistrationUpdateRequestString,
                                                                                      @PathVariable(name = "hp_profile_id") BigInteger hpProfileId) throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateHpRegistrationDetail(hpProfileId, hpRegistrationUpdateRequestString, certificate, proof);
    }

    /**
     * Retrieve health professional registration details by profile ID.
     *
     * @param hpProfileId the unique identifier of the health professional profile
     * @return an object containing the registration details of the health professional
     * @throws InvalidRequestException if the provided profile ID is invalid or missing
     * @throws WorkFlowException       if an error occurs while processing the request
     */
    @GetMapping(path = "health-professional/registration/{hp_profile_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalRegistrationDetail(hpProfileId);
    }

    /**
     * Updates the work details of a health professional in the system.
     *
     * @param hpWorkProfileUpdateRequestString The updated work details of the health professional, in JSON format.
     * @param hpProfileId                      The ID of the health professional's profile.
     * @param proof                            The proof of the updated work details, as a file.
     * @return A {@link HpProfileWorkDetailsResponseTO} object containing the updated work details.
     * @throws InvalidRequestException If the request is invalid or missing required information.
     * @throws WorkFlowException       If there is a problem with the work flow during the update process.
     */
    @PutMapping(path = "health-professional/work-profile/{hp_profile_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO updateHealthProfessionalWorkProfileDetail(@RequestPart("data") String hpWorkProfileUpdateRequestString,
                                                                                    @PathVariable(name = "hp_profile_id") BigInteger hpProfileId,
                                                                                    @RequestParam("proof") MultipartFile proof)

            throws InvalidRequestException, WorkFlowException {
        return hpService.addOrUpdateWorkProfileDetail(hpProfileId, hpWorkProfileUpdateRequestString, proof);
    }

    /**
     * Retrieves the work details of a health professional.
     *
     * @param hpProfileId The unique identifier of the health professional profile.
     * @return A {@link HpProfileWorkDetailsResponseTO} object that contains the work details of the health professional.
     * @throws InvalidRequestException If the provided hpProfileId is invalid or not found in the database.
     * @throws WorkFlowException       If an error occurs during the processing of the request.
     */
    @GetMapping(path = "health-professional/work-profile/{hp_profile_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalWorkDetail(hpProfileId);
    }

    /**
     * Adds qualifications to a healthcare provider's profile.
     *
     * @param hpProfileId                   the id of the healthcare provider's profile
     * @param qualificationDetailRequestTOs the list of qualifications to be added
     * @return a string indicating the result of the operation
     * @throws WorkFlowException if there is an error during the operation
     */
    @PostMapping(path = "/hpProfileDetail/{hp_profile_id}/qualification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addQualifications(@PathVariable(name = "hp_profile_id") BigInteger hpProfileId,
                                    @Valid @RequestBody List<QualificationDetailRequestTO> qualificationDetailRequestTOs) throws WorkFlowException {
        return hpService.addQualification(hpProfileId, qualificationDetailRequestTOs);
    }

    /**
     * Uploads the profile picture for a given HP profile.
     *
     * @param file        The profile picture file to be uploaded
     * @param hpProfileId The ID of the HP profile for which the picture is being uploaded
     * @return A response object containing information about the uploaded profile picture
     * @throws IOException If there is an error reading the file or uploading it to the server
     */
    @PostMapping(path = "/hpProfileDetail/profile_picture/{hp_profile_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePictureResponseTO uploadHpProfilePhoto(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @PathVariable(name = "hp_profile_id") BigInteger hpProfileId) throws IOException {

        return hpService.uploadHpProfilePicture(file, hpProfileId);
    }


    /**
     * Submit the health professional's profile for review.
     *
     * @param hpSubmitRequestTO The request body containing the health professional's profile information.
     * @return A response object containing the result of the submission.
     * @throws InvalidRequestException If the request body is invalid.
     * @throws WorkFlowException       If there is an issue with the submission workflow.
     */
    @PostMapping(path = "health-professional/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileAddResponseTO submit(@Valid @RequestBody HpSubmitRequestTO hpSubmitRequestTO)
            throws InvalidRequestException, WorkFlowException {
        return hpService.submitHpProfile(hpSubmitRequestTO);
    }
}
