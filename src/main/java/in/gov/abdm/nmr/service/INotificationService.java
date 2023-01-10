package in.gov.abdm.nmr.service;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.OtpException;

/**
 * Interface to declare methods
 */
public interface INotificationService {

    /**
     * Generates API
     */
    ResponseMessageTo generateOtp(OtpGenerateRequestTo otpGenerateRequestTo) throws OtpException;

    /**
     * Validates API
     */
    OtpValidateResponseTo validateOtp(OtpValidateRequestTo otpValidateRequestTo) throws OtpException;

    /**
     * Sends SMS and Email Notification to HP on each status change
     */
    ResponseMessageTo sendNotificationOnStatusChangeForHP(BigInteger hpProfileId,BigInteger workFlowStatusId);


    /**
     * Sends SMS and Email Notification to College on each status change
     */
    ResponseMessageTo sendNotificationOnStatusChangeForCollege(BigInteger collegeId,BigInteger workFlowStatusId);


}
