package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface IHpRegistrationService {

    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);
    
    HpProfileDetailResponseTO fetchHpProfileDetail(BigInteger hpProfileId);
    
    HpProfileUpdateResponseTO updateHpProfileDetail(BigInteger hpProfileId, HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException, WorkFlowException;

    HpProfileAddResponseTO addHpProfileDetail(HpProfileAddRequestTO hpProfileUpdateRequest) throws InvalidRequestException, WorkFlowException;

    HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException;

    String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs) throws WorkFlowException;

	HpProfileUpdateResponseTO updateHpPersonalDetail(BigInteger hpProfileId,
			HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException;

	HpProfileUpdateResponseTO updateHpRegistrationDetail(BigInteger hpProfileId,
			HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO);

	HpProfileUpdateResponseTO updateWorkProfileDetail(BigInteger hpProfileId,
			HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO);

}


