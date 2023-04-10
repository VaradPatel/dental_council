package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface IHpProfileDaoService {

	HpSmcDetailTO fetchSmcRegistrationDetail(Integer councilId,String registrationNumber) throws NmrException;

	HpProfileUpdateResponseTO updateHpPersonalDetails(BigInteger hpProfileId,
			HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException;

	HpProfileUpdateResponseTO updateHpRegistrationDetails(BigInteger hpProfileId,
														  HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, MultipartFile registrationCertificate, MultipartFile degreeCertificate) throws NmrException, InvalidRequestException;

	HpProfileUpdateResponseTO updateWorkProfileDetails(BigInteger hpProfileId,
													   HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<MultipartFile> proofs) throws InvalidRequestException;

	HpProfilePictureResponseTO uploadHpProfilePhoto(MultipartFile file, BigInteger hpProfileId)
			throws IOException;

	HpProfile findLatestEntryByUserid(BigInteger userId);

    HpProfile findById(BigInteger id);

    void saveQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
								  List<QualificationDetailRequestTO> qualificationDetailRequestTOS,
								  List<MultipartFile> proofs);

	ResponseMessageTo setHpProfilePhotoAndAddressThroughAadhaar(BigInteger id, AadhaarUserKycTo userKycTo);


}
