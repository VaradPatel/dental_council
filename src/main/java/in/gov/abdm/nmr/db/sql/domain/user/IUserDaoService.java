package in.gov.abdm.nmr.db.sql.domain.user;

import java.math.BigInteger;

import in.gov.abdm.nmr.db.sql.domain.user.to.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserSearchTO;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserTO;

public interface IUserDaoService {

    UserTO searchUserDetail(UserSearchTO userDetailSearchTO);

    String findRefreshTokenId(UserSearchTO userDetailSearchTO);

    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);

    User findById(BigInteger id);

    User saveUserDetail(User userDetail);

    User searchUserDetailInternal(UserSearchTO userDetailSearchTO);

    User findUserDetailByUsername(String username);
}
