package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.dto.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.dto.UserTO;

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
}
