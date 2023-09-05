package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.service.IOtpService;
import in.gov.abdm.nmr.service.IValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ValidationServiceImpl implements IValidationService {

    @Autowired
    IOtpService otpService;
    @Override
    public void validateTransactionIdForMobileNumberUpdates(String requestMobileNumber, String persistedMobileNumber, String transactionId) throws InvalidRequestException, OtpException {
        if (!Objects.equals(requestMobileNumber, persistedMobileNumber)) {
            if (transactionId == null) {
                throw new InvalidRequestException(NMRError.MISSING_MANDATORY_FIELD.getCode(), NMRError.MISSING_MANDATORY_FIELD.getMessage());
            } else if (otpService.isOtpVerified(transactionId)) {
                throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage());
            }
        }
    }
}
