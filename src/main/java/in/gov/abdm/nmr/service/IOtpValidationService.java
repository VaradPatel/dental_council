package in.gov.abdm.nmr.service;

public interface IOtpValidationService {

    boolean isOtpVerified(String id);

    boolean isOtpEnabled();
}
