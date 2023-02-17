package in.gov.abdm.nmr.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.dto.AadhaarUserKycTo;
import in.gov.abdm.nmr.dto.HpPersonalUpdateRequestTO;
import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import in.gov.abdm.nmr.dto.QualificationDetailRequestTO;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;

public interface IHpProfileDaoService {

	HpSmcDetailTO fetchSmcRegistrationDetail(Integer councilId,String registrationNumber);

	HpProfileUpdateResponseTO updateHpPersonalDetails(BigInteger hpProfileId,
			HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException;

	HpProfileUpdateResponseTO updateHpRegistrationDetails(BigInteger hpProfileId,
																 String hpRegistrationUpdateRequestTO,MultipartFile certificate, MultipartFile proof);

	HpProfileUpdateResponseTO updateWorkProfileDetails(BigInteger hpProfileId,
													   String hpWorkProfileUpdateRequestString, MultipartFile proof);

	HpProfilePictureResponseTO uploadHpProfilePhoto(MultipartFile file, BigInteger hpProfileId)
			throws InvalidRequestException, IOException;

	HpProfile findLatestEntryByUserid(BigInteger userId);

    HpProfile findById(BigInteger id);

    void saveQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
								  List<QualificationDetailRequestTO> qualificationDetailRequestTOS);

	ResponseMessageTo setHpProfilePhotoAndAddressThroughAadhaar(BigInteger id, AadhaarUserKycTo userKycTo);

    }
