package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.QualificationDetailRequestTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.exception.InvalidRequestException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IHpProfileDaoService {

    HpProfile findByUserDetail(BigInteger userDetailId);

	HpProfilePictureResponseTO uploadHpProfilePhoto(MultipartFile file, BigInteger hpProfileId)
			throws InvalidRequestException, IOException;

    HpProfile findById(BigInteger id);

    void saveQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, List<QualificationDetailRequestTO> qualificationDetailRequestTOS);
}
