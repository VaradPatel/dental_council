package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IQueriesService;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;

/**
 * The HpRegistrationController class is a RestController that handles requests for registration related operations.
 */
@RestController
@Validated
@Slf4j
public class HpRegistrationController {

    /**
     * The variable hpService holds an instance of IHpRegistrationService.
     */
    @Autowired
    private IHpRegistrationService hpService;

    @Autowired
    private IQueriesService queryService;


    /**
     * This method is used to fetch SMC registration detail information.
     *
     * @param councilId
     * @param registrationNumber
     * @return SmcRegistrationDetailResponseTO This returns the SMC registration detail information.
     */
    @GetMapping(path = "health-professional", produces = MediaType.APPLICATION_JSON_VALUE)
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(
            @RequestParam("smcId") Integer councilId,
            @RequestParam("registrationNumber") String registrationNumber) throws NmrException {
        return hpService.fetchSmcRegistrationDetail(councilId, registrationNumber);
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

        log.info("In HP Registration Controller: addHealthProfessionalPersonalDetail method ");
        log.debug("Request Payload: HpPersonalUpdateRequestTO: ");
        log.debug(hpPersonalUpdateRequestTO.toString());

        HpProfilePersonalResponseTO hpProfilePersonalResponseTO = hpService.addOrUpdateHpPersonalDetail(null, hpPersonalUpdateRequestTO);

        log.info("HP Registration Controller: addHealthProfessionalPersonalDetail method: Execution Successful. ");
        log.debug("Response Payload: HpProfilePersonalResponseTO: ");
        log.debug(hpProfilePersonalResponseTO.toString());

        return hpProfilePersonalResponseTO;
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
    @PutMapping(path = "health-professional/{healthProfessionalId}/personal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO updateHealthProfessionalPersonalDetail(
            @Valid @RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO,
            @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {

        log.info("In HP Registration Controller: updateHealthProfessionalPersonalDetail method ");
        log.debug("Request Payload: HpPersonalUpdateRequestTO: ");
        log.debug(hpPersonalUpdateRequestTO.toString());

        HpProfilePersonalResponseTO hpProfilePersonalResponseTO = hpService.addOrUpdateHpPersonalDetail(hpProfileId, hpPersonalUpdateRequestTO);

        log.info("HP Registration Controller: updateHealthProfessionalPersonalDetail method: Execution Successful. ");
        log.debug("Response Payload: HpProfilePersonalResponseTO: ");
        log.debug(hpProfilePersonalResponseTO.toString());

        return hpProfilePersonalResponseTO;
    }

    /**
     * This method is used to get the personal details of a health professional based on the hp_profile_id.
     *
     * @param hpProfileId The unique identifier of the health professional.
     * @return An instance of {@link HpProfilePersonalResponseTO} containing the personal details of the health professional.
     * @throws InvalidRequestException If the request is invalid.
     * @throws WorkFlowException       If there is a problem with the workflow.
     */
    @GetMapping(path = "health-professional/{healthProfessionalId}/personal", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalPersonalDetail(hpProfileId);
    }

    /**
     * This method updates the health professional registration details.
     *
     * @param registrationCertificate                       The health professional's certificate file.
     * @param degreeCertificate                             The health professional's proof file.
     * @param hpRegistrationUpdateRequestTO The health professional registration update request in string format.
     * @param hpProfileId                       The health professional profile id.
     * @return The updated health professional registration response in a transfer object.
     * @throws InvalidRequestException If the provided request is invalid.
     * @throws WorkFlowException       If there is an error in the workflow.
     */
    @PutMapping(path = "health-professional/{healthProfessionalId}/registration", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO updateHealthProfessionalRegistrationDetail(@RequestParam(value = "registrationCertificate", required = false) MultipartFile registrationCertificate,
                                                                                      @RequestParam(value = "degreeCertificate", required = false) MultipartFile degreeCertificate,
                                                                                      @RequestPart("data") HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO,
                                                                                      @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) throws InvalidRequestException, WorkFlowException, NmrException {

        log.info("In HP Registration Controller: updateHealthProfessionalRegistrationDetail method ");
        log.debug("Request Payload: HpRegistrationUpdateRequestTO: ");
        log.debug(hpRegistrationUpdateRequestTO.toString());

        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = hpService.addOrUpdateHpRegistrationDetail(hpProfileId, hpRegistrationUpdateRequestTO, registrationCertificate, degreeCertificate);

        log.info("HP Registration Controller: updateHealthProfessionalRegistrationDetail method: Execution Successful. ");
        log.debug("Response Payload: HpProfileRegistrationResponseTO: ");
        log.debug(hpProfileRegistrationResponseTO.toString());

        return hpProfileRegistrationResponseTO;
    }

    /**
     * Retrieve health professional registration details by profile ID.
     *
     * @param hpProfileId the unique identifier of the health professional profile
     * @return an object containing the registration details of the health professional
     * @throws InvalidRequestException if the provided profile ID is invalid or missing
     * @throws WorkFlowException       if an error occurs while processing the request
     */
    @GetMapping(path = "health-professional/{healthProfessionalId}/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {
        return hpService.getHealthProfessionalRegistrationDetail(hpProfileId);
    }

    /**
     * Updates the work details of a health professional in the system.
     *
     * @param hpWorkProfileUpdateRequestTO The updated work details of the health professional, in JSON format.
     * @param hpProfileId                      The ID of the health professional's profile.
     * @return A {@link HpProfileWorkDetailsResponseTO} object containing the updated work details.
     * @throws InvalidRequestException If the request is invalid or missing required information.
     * @throws WorkFlowException       If there is a problem with the work flow during the update process.
     */
    @PutMapping(path = "health-professional/{healthProfessionalId}/work-profile", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO updateHealthProfessionalWorkProfileDetail(@RequestBody HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO,
                                                                                    @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException, NmrException {

        log.info("In HP Registration Controller: updateHealthProfessionalWorkProfileDetail method ");
        log.debug("Request Payload: HpWorkProfileUpdateRequestTO: ");
        log.debug(hpWorkProfileUpdateRequestTO.toString());

        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = hpService.addOrUpdateWorkProfileDetail(hpProfileId, hpWorkProfileUpdateRequestTO, Collections.emptyList());

        log.info("HP Registration Controller: updateHealthProfessionalWorkProfileDetail method: Execution Successful. ");
        log.debug("Response Payload: HpProfileWorkDetailsResponseTO: ");
        log.debug(hpProfileWorkDetailsResponseTO.toString());

        return hpProfileWorkDetailsResponseTO;
    }

    /**
     * Retrieves the work details of a health professional.
     *
     * @param hpProfileId The unique identifier of the health professional profile.
     * @return A {@link HpProfileWorkDetailsResponseTO} object that contains the work details of the health professional.
     * @throws InvalidRequestException If the provided hpProfileId is invalid or not found in the database.
     * @throws WorkFlowException       If an error occurs during the processing of the request.
     */
    @GetMapping(path = "health-professional/{healthProfessionalId}/work-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException, NmrException {
        return hpService.getHealthProfessionalWorkDetail(hpProfileId);
    }

    /**
     * Adds qualifications to a healthcare provider's profile.
     *
     * @param hpProfileId                   the id of the healthcare provider's profile
     * @param qualificationDetailRequestTO the list of qualifications to be added
     * @return a string indicating the result of the operation
     * @throws WorkFlowException if there is an error during the operation
     */
    @PostMapping(path = "/health-professional/{healthProfessionalId}/qualifications", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addQualifications(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId,
                                    @RequestPart("data") QualificationRequestTO qualificationDetailRequestTO,
                                    @RequestParam(value = "degreeCertificates") List<MultipartFile> degreeCertificates
                                    ) throws WorkFlowException, InvalidRequestException, NmrException {
        log.info(degreeCertificates!=null?String.valueOf(degreeCertificates.size()):null);
        return hpService.addQualification(hpProfileId, qualificationDetailRequestTO.getQualificationDetailRequestTos(), degreeCertificates);
    }

    /**
     * Uploads the profile picture for a given HP profile.
     * @param file        The profile picture file to be uploaded
     * @param hpProfileId The ID of the HP profile for which the picture is being uploaded
     * @return A response object containing information about the uploaded profile picture
     * @throws IOException If there is an error reading the file or uploading it to the server
     */
    @PostMapping(path = "/health-professional/{healthProfessionalId}/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePictureResponseTO uploadHpProfilePhoto(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) throws IOException {
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
    @PostMapping(path = "health-professional/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileAddResponseTO submit(@Valid @RequestBody HpSubmitRequestTO hpSubmitRequestTO)
            throws InvalidRequestException, WorkFlowException {

        log.info("In HP Registration Controller: submit method ");
        log.debug("Request Payload: HpSubmitRequestTO: ");
        log.debug(hpSubmitRequestTO.toString());

        HpProfileAddResponseTO hpProfileAddResponseTO = hpService.submitHpProfile(hpSubmitRequestTO);

        log.info("HP Registration Controller: updateHealthProfessionalWorkProfileDetail method: Execution Successful. ");
        log.debug("Response Payload: HpProfileAddResponseTO: ");
        log.debug(hpProfileAddResponseTO.toString());

        return hpProfileAddResponseTO;

    }

    /**
     * Endpoint to create multiple queries at a time
     *
     * @param queryCreateTo coming from user
     * @return returns created list of queries
     */
    @PostMapping(path = NMRConstants.RAISE_QUERY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo raiseQuery(@Valid @RequestBody QueryCreateTo queryCreateTo) throws WorkFlowException {
        return queryService.createQueries(queryCreateTo);
    }

    /**
     * Endpoint to get all the queries against hpProfileId
     *
     * @param healthProfessionalId takes hpProfileId as a input
     * @return returns list of queries associated with hpProfileId
     */
    @GetMapping(path = NMRConstants.GET_QUERIES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<QueryResponseTo> getQueries(@PathVariable("healthProfessionalId") BigInteger healthProfessionalId) {
        return queryService.getQueriesByHpProfileId(healthProfessionalId);
    }

    @PostMapping(path = NMRConstants.SAVE_KYC_DETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
    public KycResponseMessageTo saveUserKycDetails(@PathVariable("registrationNumber") String registrationNumber,@RequestBody UserKycTo userKycTo){
        return hpService.saveUserKycDetails(registrationNumber,userKycTo);
    }

    @PostMapping(path = "health-professional", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessageTo> addNewHealthProfessional(@Valid @RequestBody NewHealthPersonalRequestTO request) throws DateException {
        hpService.addNewHealthProfessional(request);
        return ResponseEntity.ok(ResponseMessageTo.builder().message(SUCCESS_RESPONSE).build());
    }

    @PatchMapping(path = "health-professional/{healthProfessionalId}/personal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessageTo> updateHealthProfessionalEmailMobile(
            @RequestBody HealthProfessionalPersonalRequestTo request,
            @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) throws OtpException, InvalidRequestException {
        hpService.updateHealthProfessionalEmailMobile(hpProfileId, request);
        return ResponseEntity.ok(ResponseMessageTo.builder().message(SUCCESS_RESPONSE).build());
    }

    /**
     * Sends email verification link on email/mobile
     *
     * @param verifyEmailLinkTo receiver email/mobile
     * @return Success/Fail message
     */

    @PatchMapping(path = "health-professional/{healthProfessionalId}/email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo getVerifyEmailLink(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId, @RequestBody VerifyEmailLinkTo verifyEmailLinkTo) {

        return hpService.getEmailVerificationLink(hpProfileId,verifyEmailLinkTo);

    }
}
