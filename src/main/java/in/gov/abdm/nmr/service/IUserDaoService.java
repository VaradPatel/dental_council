package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.NmrException;

import java.math.BigInteger;

public interface IUserDaoService {

    UserTO searchUserDetail(UserSearchTO userDetailSearchTO);

    String findRefreshTokenId(UserSearchTO userDetailSearchTO);

    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);

    User findById(BigInteger id);

    User saveUserDetail(User userDetail);

    User searchUserDetailInternal(UserSearchTO userDetailSearchTO);

    User findByUsername(String username);

    User toggleSmsNotification(boolean isSmsNotificationEnabled);

    User toggleEmailNotification(boolean isEmailNotificationEnabled);

    User toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

    SMCProfile findSmcProfile(BigInteger id) throws NmrException;

    NmcProfile findNmcProfile(BigInteger id) throws NmrException;

    NbeProfile findNbeProfile(BigInteger id) throws NmrException;

    SMCProfile updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException;

    NmcProfile updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException;

    NbeProfile updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException;
}
