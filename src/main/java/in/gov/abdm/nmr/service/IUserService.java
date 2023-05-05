package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.OtpException;

import java.math.BigInteger;
import java.util.List;

public interface IUserService {

    NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled);

    NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled);

    List<NotificationToggleResponseTO> toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

    SMCProfileTO getSmcProfile(BigInteger id) throws NmrException, InvalidIdException;

    NmcProfileTO getNmcProfile(BigInteger id) throws NmrException, InvalidIdException;

    NbeProfileTO getNbeProfile(BigInteger id) throws NmrException, InvalidIdException;

    SMCProfileTO updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException, InvalidIdException;

    NmcProfileTO updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException, InvalidIdException;

    NbeProfileTO updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException, InvalidIdException;

    String retrieveUser(RetrieveUserRequestTo retrieveUserRequestTo) throws OtpException;

    ResponseMessageTo verifyEmail(VerifyEmailTo verifyEmailTo);
}
