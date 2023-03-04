package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.OtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateResponseTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.exception.OtpException;

import java.security.GeneralSecurityException;

/**
 * Interface to declare methods
 */
public interface IOtpService {

    ResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws OtpException;

    OtpValidateResponseTo validateOtp(OtpValidateRequestTo otpValidateRequestTo, boolean callInternal) throws OtpException, GeneralSecurityException;

}
