package in.gov.abdm.nmr.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import in.gov.abdm.nmr.exception.WorkFlowException;
import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.dto.HpProfileAddResponseTO;
import in.gov.abdm.nmr.dto.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;

public interface IHpRegistrationService {

    SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO);
    
    HpProfileDetailResponseTO fetchHpProfileDetail(BigInteger hpProfileId);
    
    HpProfileUpdateResponseTO updateHpProfileDetail(BigInteger hpProfileId, HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException;

    HpProfileAddResponseTO addHpProfileDetail(HpProfileAddRequestTO hpProfileUpdateRequest) throws InvalidRequestException, WorkFlowException;

    HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException;
}


