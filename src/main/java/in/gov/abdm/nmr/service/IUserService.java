package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidIDException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.OtpException;

import java.math.BigInteger;
import java.util.List;

public interface IUserService {

    NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled);

    NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled);

    List<NotificationToggleResponseTO> toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

    SMCProfileTO getSmcProfile(BigInteger id) throws NmrException, InvalidIDException;

    NmcProfileTO getNmcProfile(BigInteger id) throws NmrException, InvalidIDException;

    NbeProfileTO getNbeProfile(BigInteger id) throws NmrException, InvalidIDException;

    SMCProfileTO updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException, InvalidIDException;

    NmcProfileTO updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException, InvalidIDException;

    NbeProfileTO updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException, InvalidIDException;

    ResponseMessageTo createHpUserAccount(CreateHpUserAccountTo createHpUserAccountTo);

    String retrieveUser(RetrieveUserRequestTo retrieveUserRequestTo) throws OtpException;

    ResponseMessageTo verifyEmail(VerifyEmailTo verifyEmailTo);
}
