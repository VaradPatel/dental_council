package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.OtpException;

import java.math.BigInteger;
import java.util.List;

public interface IUserService {

    NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled);

    NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled);

    List<NotificationToggleResponseTO> toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

    SMCProfileTO getSmcProfile(BigInteger id) throws NmrException;

    NmcProfileTO getNmcProfile(BigInteger id) throws NmrException;

    NbeProfileTO getNbeProfile(BigInteger id) throws NmrException;

    SMCProfileTO updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException;

    NmcProfileTO updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException;

    NbeProfileTO updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException;

    ResponseMessageTo createHpUserAccount(CreateHpUserAccountTo createHpUserAccountTo);

    String retrieveUser(RetrieveUserRequestTo retrieveUserRequestTo) throws OtpException;
}
