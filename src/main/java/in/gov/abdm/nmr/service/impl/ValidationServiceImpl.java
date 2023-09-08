package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.service.IOtpValidationService;
import in.gov.abdm.nmr.service.IValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ValidationServiceImpl implements IValidationService {

    @Autowired
    IOtpValidationService otpValidationService;
    @Override
    public void validateTransactionIdForMobileNumberUpdates(String requestMobileNumber, String persistedMobileNumber, String transactionId) throws InvalidRequestException, OtpException {
        if (!Objects.equals(requestMobileNumber, persistedMobileNumber)) {
            if (transactionId == null) {
                throw new InvalidRequestException(NMRError.VERIFY_MOBILE_NUMBER.getCode(), NMRError.VERIFY_MOBILE_NUMBER.getMessage());
            } else if (otpValidationService.isOtpVerified(transactionId)) {
                throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage());
            }
        }
    }

}
