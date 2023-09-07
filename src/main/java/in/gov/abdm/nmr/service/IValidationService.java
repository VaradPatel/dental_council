package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.OtpException;

public interface IValidationService {

    public void validateTransactionIdForMobileNumberUpdates(String requestMobileNumber, String persistedMobileNumber, String transactionId) throws InvalidRequestException, OtpException;
}
