package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.exception.InvalidRequestException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.exception.WorkFlowException;
import org.springframework.web.multipart.MultipartFile;

public interface IHpProfileDaoService {

	HpSmcDetailTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);

	HpProfileUpdateResponseTO updateHpPersonalDetails(BigInteger hpProfileId,
			HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException;

	HpProfileUpdateResponseTO updateHpRegistrationDetails(BigInteger hpProfileId,
																 HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO);

	HpProfileUpdateResponseTO updateWorkProfileDetails(BigInteger hpProfileId,
													   HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO);

	HpProfilePictureResponseTO uploadHpProfilePhoto(MultipartFile file, BigInteger hpProfileId)
			throws InvalidRequestException, IOException;

	HpProfile findByUserDetail(BigInteger userDetailId);

    HpProfile findById(BigInteger id);

    void saveQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
								  List<QualificationDetailRequestTO> qualificationDetailRequestTOS);

	ResponseMessageTo setHpProfilePhotoAndAddressThroughAadhaar(BigInteger id, AadhaarUserKycTo userKycTo);





}
