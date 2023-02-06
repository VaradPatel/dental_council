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

    /**
     * The fetchSmcRegistrationDetail method is used to retrieve the SMC registration details of a user.
     *
     * @param smcRegistrationDetailRequestTO The SMC registration detail request transfer object which contains the required information to retrieve the SMC registration details.
     * @return SmcRegistrationDetailResponseTO The SMC registration detail response transfer object which contains the SMC registration details of the user.
     */
    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);
    
    HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException;

    String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs) throws WorkFlowException;

    HpProfilePersonalResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
                                                          HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException;

    HpProfileRegistrationResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
                                                              String hpRegistrationUpdateRequestTO,MultipartFile certificate,MultipartFile proof);

    HpProfileWorkDetailsResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
                                                           String hpWorkProfileUpdateRequestTO, MultipartFile proof);

    HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO) throws InvalidRequestException, WorkFlowException;

    HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(BigInteger hpProfileId);

    HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId);

    HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(BigInteger hpProfileId);
}


