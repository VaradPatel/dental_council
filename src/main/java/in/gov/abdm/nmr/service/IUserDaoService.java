package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.dto.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.dto.UserTO;
import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.NmrException;

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
}
