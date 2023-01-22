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

    HpProfilePersonalResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
                                                          HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException;

    HpProfileRegistrationResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
                                                              HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO);

    HpProfileWorkDetailsResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
                                                           HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO);

    HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO) throws InvalidRequestException, WorkFlowException;

    HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(BigInteger hpProfileId);

    HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId);

    HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(BigInteger hpProfileId);
}


