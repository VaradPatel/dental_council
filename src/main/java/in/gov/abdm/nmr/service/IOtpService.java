package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.exception.TemplateException;

import java.security.GeneralSecurityException;

/**
 * Interface to declare methods
 */
public interface IOtpService {

    OTPResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws OtpException, InvalidRequestException, TemplateException;

    OtpValidateResponseTo validateOtp(OtpValidateRequestTo otpValidateRequestTo, boolean callInternal) throws OtpException, GeneralSecurityException;

    boolean isOtpVerified(String id);

    boolean isOtpEnabled();
}
