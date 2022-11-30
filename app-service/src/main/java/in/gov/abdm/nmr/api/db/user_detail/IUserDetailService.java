package in.gov.abdm.nmr.api.db.user_detail;

import in.gov.abdm.nmr.api.db.user_detail.to.UserDetailSearchTO;
import in.gov.abdm.nmr.api.db.user_detail.to.UserDetailTO;

public interface IUserDetailService {

    UserDetailTO searchUserDetail(UserDetailSearchTO userDetailSearchTO);

    String findRefreshTokenId(UserDetailSearchTO userDetailSearchTO);

    Integer updateRefreshTokenId(String username, String refreshTokenId);
}
