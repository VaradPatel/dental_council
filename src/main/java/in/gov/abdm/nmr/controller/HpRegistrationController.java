package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.security.ChecksumUtil;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.ICouncilService;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IQueriesService;
import in.gov.abdm.nmr.util.NMRConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;

/**
 * The HpRegistrationController class is a RestController that handles requests for registration related operations.
 */
@RestController
@Validated
@Slf4j
@CrossOrigin
public class HpRegistrationController {

    /**
     * The variable hpService holds an instance of IHpRegistrationService.
     */
    @Autowired
    private IHpRegistrationService hpService;

    @Autowired
    private IQueriesService queryService;

    @Autowired
    private ICouncilService councilService;

    @Autowired
    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    @Autowired
    ChecksumUtil checksumUtil;

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
            @RequestParam("registrationNumber") @NotBlank @Size(max = 100) String registrationNumber) throws NoDataFoundException {
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

        checksumUtil.validateChecksum();
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
    @PutMapping(path = ProtectedPaths.HEALTH_PROFESSIONAL_PERSONAL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO updateHealthProfessionalPersonalDetail(
            @Valid @RequestBody HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO,
            @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId)
            throws InvalidRequestException, WorkFlowException {

        checksumUtil.validateChecksum();
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
     */
    @GetMapping(path = ProtectedPaths.HEALTH_PROFESSIONAL_PERSONAL, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) {
        return hpService.getHealthProfessionalPersonalDetail(hpProfileId);
    }

    /**
     * This method updates the health professional registration details.
     *
     * @param registrationCertificate       The health professional's certificate file.
     * @param degreeCertificate             The health professional's proof file.
     * @param hpRegistrationUpdateRequestTO The health professional registration update request in string format.
     * @param hpProfileId                   The health professional profile id.
     * @return The updated health professional registration response in a transfer object.
     * @throws InvalidRequestException If the provided request is invalid.
     */
    @PutMapping(path = ProtectedPaths.HEALTH_PROFESSIONAL_REGISTRATION, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO updateHealthProfessionalRegistrationDetail(@RequestParam(value = "registrationCertificate", required = false) MultipartFile registrationCertificate,
                                                                                      @RequestParam(value = "degreeCertificate", required = false) MultipartFile degreeCertificate,
                                                                                      @RequestPart("data") @Valid HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO,
                                                                                      @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId,
                                                                                      @RequestParam(value = "proofOfQualificationNameChange", required = false) MultipartFile proofOfQualificationNameChange,
                                                                                      @RequestParam(value = "proofOfRegistrationNameChange", required = false) MultipartFile proofOfRegistrationNameChange) throws InvalidRequestException, NmrException {

        checksumUtil.validateChecksum();
        log.info("In HP Registration Controller: updateHealthProfessionalRegistrationDetail method ");
        log.debug("Request Payload: HpRegistrationUpdateRequestTO: ");
        log.debug(hpRegistrationUpdateRequestTO.toString());

        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = hpService.addOrUpdateHpRegistrationDetail(hpProfileId, hpRegistrationUpdateRequestTO, registrationCertificate, degreeCertificate != null ? List.of(degreeCertificate) : Collections.emptyList(), proofOfQualificationNameChange, proofOfRegistrationNameChange);

        log.info("HP Registration Controller: updateHealthProfessionalRegistrationDetail method: Execution Successful. ");
        log.debug("Response Payload: HpProfileRegistrationResponseTO: ");
        log.debug(hpProfileRegistrationResponseTO.toString());

        return hpProfileRegistrationResponseTO;
    }


/*
    @PutMapping(path = "/v2/health-professional/{healthProfessionalId}/registration", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO updateHealthProfessionalRegistrationDetail(@RequestParam(value = "registrationCertificate", required = false) MultipartFile registrationCertificate,
                                                                                      @RequestParam(value = "degreeCertificate", required = false) List<MultipartFile> degreeCertificate,
                                                                                      @RequestPart("data") @Valid HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO,
                                                                                      @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) throws InvalidRequestException, NmrException {

        checksumUtil.validateChecksum();
        log.info("In HP Registration Controller: updateHealthProfessionalRegistrationDetail method ");
        log.debug("Request Payload: HpRegistrationUpdateRequestTO: ");
        log.debug(hpRegistrationUpdateRequestTO.toString());

        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = hpService.addOrUpdateHpRegistrationDetail(hpProfileId, hpRegistrationUpdateRequestTO, registrationCertificate, degreeCertificate);

        log.info("HP Registration Controller: updateHealthProfessionalRegistrationDetail method: Execution Successful. ");
        log.debug("Response Payload: HpProfileRegistrationResponseTO: ");
        log.debug(hpProfileRegistrationResponseTO.toString());

        return hpProfileRegistrationResponseTO;
    }
*/

    /**
     * Retrieve health professional registration details by profile ID.
     *
     * @param hpProfileId the unique identifier of the health professional profile
     * @return an object containing the registration details of the health professional
     */
    @GetMapping(path = ProtectedPaths.HEALTH_PROFESSIONAL_REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) {
        return hpService.getHealthProfessionalRegistrationDetail(hpProfileId);
    }

    /**
     * Updates the work details of a health professional in the system.
     *
     * @param hpWorkProfileUpdateRequestTO The updated work details of the health professional, in JSON format.
     * @param hpProfileId                  The ID of the health professional's profile.
     * @return A {@link HpProfileWorkDetailsResponseTO} object containing the updated work details.
     * @throws InvalidRequestException If the request is invalid or missing required information.
     */
    @PutMapping(path = ProtectedPaths.HEALTH_PROFESSIONAL_WORK_PROFILE, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO updateHealthProfessionalWorkProfileDetail(@RequestBody @Valid HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO,
                                                                                    @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId)
            throws InvalidRequestException, NmrException, NotFoundException {

        checksumUtil.validateChecksum();
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
    @GetMapping(path = ProtectedPaths.HEALTH_PROFESSIONAL_WORK_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) {
        return hpService.getHealthProfessionalWorkDetail(hpProfileId);
    }

    /**
     * Adds qualifications to a healthcare provider's profile.
     *
     * @param hpProfileId                  the id of the healthcare provider's profile
     * @param qualificationDetailRequestTO the list of qualifications to be added
     * @return a string indicating the result of the operation
     * @throws WorkFlowException if there is an error during the operation
     * @throws InvalidFileUploadException
     * @throws IOException 
     */
    @PostMapping(path = ProtectedPaths.ADDITIONAL_QUALIFICATION, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addQualifications(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId,
                                    @RequestPart("data") @Valid QualificationRequestTO qualificationDetailRequestTO,
                                    @RequestParam(value = "degreeCertificates") List<MultipartFile> degreeCertificates,
                                    @RequestParam(value = "proofOfQualificationNameChange", required = false) MultipartFile proofOfQualificationNameChange
    ) throws WorkFlowException, InvalidRequestException, IOException, InvalidFileUploadException {
        checksumUtil.validateChecksum();

        log.info(degreeCertificates != null ? String.valueOf(degreeCertificates.size()) : null);
        return hpService.addQualification(hpProfileId, qualificationDetailRequestTO.getQualificationDetailRequestTos(), degreeCertificates, proofOfQualificationNameChange);
    }

    /**
     * Update qualifications to a healthcare provider's profile.
     *
     * @param hpProfileId                  the id of the healthcare provider's profile
     * @param qualificationDetailRequestTO the list of qualifications to be added
     * @return a string indicating the result of the operation
     * @throws WorkFlowException if there is an error during the operation
     * @throws IOException 
     * @throws InvalidFileUploadException
     */
    @PutMapping(path = ProtectedPaths.ADDITIONAL_QUALIFICATION, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateQualifications(@PathVariable(name = "healthProfessionalId") BigInteger hpProfileId,
                                    @RequestPart("data") @Valid QualificationRequestTO qualificationDetailRequestTO,
                                    @RequestParam(value = "degreeCertificates", required = false) List<MultipartFile> degreeCertificates
    ) throws InvalidRequestException, WorkFlowException, IOException, InvalidFileUploadException {
        checksumUtil.validateChecksum();
        log.info(degreeCertificates != null ? String.valueOf(degreeCertificates.size()) : null);
        return hpService.updateQualification(hpProfileId, qualificationDetailRequestTO.getQualificationDetailRequestTos(),degreeCertificates);
    }

    /**
     * Uploads the profile picture for a given HP profile.
     *
     * @param file        The profile picture file to be uploaded
     * @param hpProfileId The ID of the HP profile for which the picture is being uploaded
     * @return A response object containing information about the uploaded profile picture
     * @throws IOException If there is an error reading the file or uploading it to the server
     * @throws InvalidFileUploadException
     */
    @PostMapping(path = ProtectedPaths.UPDATE_PROFILE_PICTURE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfilePictureResponseTO uploadHpProfilePhoto(
            @RequestParam(value = "file", required = true) MultipartFile file,
            @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) throws IOException, InvalidRequestException, InvalidFileUploadException {
        checksumUtil.validateChecksum();
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
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = ProtectedPaths.HP_REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HpProfileAddResponseTO submit(@Valid @RequestBody HpSubmitRequestTO hpSubmitRequestTO)
            throws InvalidRequestException, WorkFlowException {

        checksumUtil.validateChecksum();
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
    @PostMapping(path = ProtectedPaths.RAISE_QUERY, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo raiseQuery(@Valid @RequestBody QueryCreateTo queryCreateTo) throws WorkFlowException, InvalidRequestException {
        checksumUtil.validateChecksum();
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

    @PostMapping(path = NMRConstants.KYC_FUZZY_MATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public KycResponseMessageTo saveUserKycDetails(@PathVariable("registrationNumber") String registrationNumber,
                                                   @RequestParam("councilId") BigInteger councilId,
                                                   @RequestBody UserKycTo userKycTo) throws ParseException {
        checksumUtil.validateChecksum();
        StateMedicalCouncil stateMedicalCouncil = stateMedicalCouncilRepository.findStateMedicalCouncilById(councilId);
        List<Council> imrRecords = councilService.getCouncilByRegistrationNumberAndCouncilName(registrationNumber, stateMedicalCouncil.getName());
        return hpService.userKycFuzzyMatch(imrRecords, registrationNumber, councilId, userKycTo.getName(),userKycTo.getGender(),userKycTo.getBirthDate());
    }

    @PostMapping(path = "health-professional", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo addNewHealthProfessional(@Valid @RequestBody NewHealthPersonalRequestTO request) throws DateException, ParseException, GeneralSecurityException, InvalidRequestException, NmrException {
        checksumUtil.validateChecksum();
        return hpService.addNewHealthProfessional(request);
    }

    @PatchMapping(path = "health-professional/{healthProfessionalId}/personal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessageTo> updateHealthProfessionalEmailMobile(
            @Valid @RequestBody HealthProfessionalPersonalRequestTo request,
            @PathVariable(name = "healthProfessionalId") BigInteger hpProfileId) throws OtpException, InvalidRequestException {
        checksumUtil.validateChecksum();
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
        checksumUtil.validateChecksum();
        return hpService.getEmailVerificationLink(hpProfileId,verifyEmailLinkTo);
    }

    @DeleteMapping(path = ProtectedPaths.DE_LINK_FACILITY)
    public ResponseEntity<ResponseMessageTo> delinkCurrentWorkDetails(@RequestBody WorkDetailsDelinkRequest workDetailsDelinkRequest) throws NmrException {
        checksumUtil.validateChecksum();
        hpService.delinkCurrentWorkDetails(workDetailsDelinkRequest);
        return ResponseEntity.ok(ResponseMessageTo.builder().message(SUCCESS_RESPONSE).build());
    }
}