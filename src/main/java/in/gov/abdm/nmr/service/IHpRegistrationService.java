package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;

/**
 * The IHpRegistrationService interface defines the methods that must be implemented by a service
 * that performs registration operations for a Health Provider system.
 */
public interface IHpRegistrationService {

    /**
     * The fetchSmcRegistrationDetail method is used to retrieve the SMC registration details of a user.
     *
     * @param councilId
     * @param registrationNumber
     * @return SmcRegistrationDetailResponseTO The SMC registration detail response transfer object which contains the SMC registration details of the user.
     */
    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber);

    /**
     * Uploads the profile picture for the given HP profile ID.
     *
     * @param file        The file to be uploaded as the profile picture.
     * @param hpProfileId The ID of the HP profile to upload the picture for.
     * @return An instance of {@link HpProfilePictureResponseTO} containing information about the uploaded profile picture.
     * @throws IOException If there is an error reading the file or uploading it to the server.
     */
    HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException;

    /**
     * Adds qualifications to a HP profile.
     *
     * @param hpProfileId                   The ID of the HP profile to which the qualifications will be added.
     * @param qualificationDetailRequestTOs A list of qualification details to be added to the HP profile.
     * @return A string indicating the status of the operation.
     * @throws WorkFlowException If an error occurs during the qualification addition process.
     */
    String addQualification(BigInteger hpProfileId, String qualificationDetailRequestTOsString) throws WorkFlowException;

    /**
     * Adds or updates the personal details for a given HP profile.
     *
     * @param hpProfileId               the id of the HP profile to be updated.
     * @param hpPersonalUpdateRequestTO the request object containing the updated personal details.
     * @return the updated HP profile personal response object.
     * @throws InvalidRequestException if the request parameters are invalid.
     * @throws WorkFlowException       if an error occurs during the workflow process.
     */
    HpProfilePersonalResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
                                                            HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException;

    /**
     * Adds or updates the HP profile registration details.
     *
     * @param hpProfileId The unique identifier of the HP profile.
     * @return The response object that contains the status of the operation.
     * @throws Exception If any error occurs during the operation.
     */
    HpProfileRegistrationResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
                                                                    String hpRegistrationUpdateRequestTO, MultipartFile certificate, MultipartFile proof);

    /**
     * Adds or updates work profile details for a given health professional profile.
     *
     * @param hpProfileId                  The unique identifier of the health professional profile.
     * @param hpWorkProfileUpdateRequestTO A string representation of the update request object, containing the work profile information.
     * @param proof                        A multipart file containing proof of work experience.
     * @return An object containing the updated work profile details.
     * @throws Exception In case of any error during the update process.
     */
    HpProfileWorkDetailsResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
                                                                String hpWorkProfileUpdateRequestTO, MultipartFile proof);

    /**
     * Submit the HP profile using the provided request data.
     *
     * @param hpSubmitRequestTO the request data to submit the HP profile
     * @return the response with the status of the HP profile submission
     * @throws InvalidRequestException if the request data is invalid
     * @throws WorkFlowException       if an error occurs during the submission process
     */
    HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO) throws InvalidRequestException, WorkFlowException;

    /**
     * Retrieves the personal details of a health professional associated with a specific profile id.
     *
     * @param hpProfileId The unique identifier for a health professional's profile.
     * @return An object of HpProfilePersonalResponseTO, which contains the personal details of a health professional.
     * @throws Exception If the input profile id is invalid or if the personal details of the health professional are not found.
     */
    HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(BigInteger hpProfileId);

    /**
     * Retrieves the work details of a health professional.
     *
     * @param hpProfileId The unique identifier for the health professional's profile.
     * @return An instance of {@link HpProfileWorkDetailsResponseTO} containing the work details of the health professional.
     */
    HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId);

    /**
     * This method is used to retrieve the registration details of a health professional based on their profile ID.
     *
     * @param hpProfileId A unique identifier for the health professional's profile.
     * @return HpProfileRegistrationResponseTO object containing the registration details of the health professional.
     */
    HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(BigInteger hpProfileId);
}
