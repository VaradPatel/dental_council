package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface IHpRegistrationService {

    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);
    
    HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException;

    String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs) throws WorkFlowException;

	HpProfileUpdateResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
                                                          HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException;

	HpProfileUpdateResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
                                                              HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO);

	HpProfileUpdateResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
                                                           HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO);

    HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO) throws InvalidRequestException, WorkFlowException;

}


