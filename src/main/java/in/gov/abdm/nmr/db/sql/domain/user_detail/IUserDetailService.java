package in.gov.abdm.nmr.db.sql.domain.user_detail;

import java.math.BigInteger;

import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UserDetailSearchTO;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UserDetailTO;

public interface IUserDetailService {

    UserDetailTO searchUserDetail(UserDetailSearchTO userDetailSearchTO);

    String findRefreshTokenId(UserDetailSearchTO userDetailSearchTO);

    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);

    User findById(BigInteger id);

    User saveUserDetail(User user);

    User searchUserDetailInternal(UserDetailSearchTO userDetailSearchTO);

    User findUserDetailByUsername(String username);
}
