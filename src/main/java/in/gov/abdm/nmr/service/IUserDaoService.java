package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.dto.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.dto.UserTO;
import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;

import java.math.BigInteger;

public interface IUserDaoService {

    UserTO searchUserDetail(UserSearchTO userDetailSearchTO);

    String findRefreshTokenId(UserSearchTO userDetailSearchTO);

    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);

    User findById(BigInteger id);

    User saveUserDetail(User userDetail);

    User searchUserDetailInternal(UserSearchTO userDetailSearchTO);

    User findUserDetailByUsername(String username);

    User toggleSmsNotification(boolean isSmsNotificationEnabled);

    User toggleEmailNotification(boolean isEmailNotificationEnabled);
    User toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);
    SMCProfile findSmcProfileByUserId(BigInteger userId);
   NmcProfile findNmcProfileByUserId(BigInteger userId);
   NbeProfile findNbeProfileByUserId(BigInteger userId);
}
