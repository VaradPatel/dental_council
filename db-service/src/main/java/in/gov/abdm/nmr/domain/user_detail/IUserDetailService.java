package in.gov.abdm.nmr.domain.user_detail;

import in.gov.abdm.nmr.domain.user_detail.to.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.domain.user_detail.to.UserDetailSearchTO;
import in.gov.abdm.nmr.domain.user_detail.to.UserDetailTO;

public interface IUserDetailService {

    UserDetailTO searchUserDetail(UserDetailSearchTO userDetailSearchTO);

    String findRefreshTokenId(UserDetailSearchTO userDetailSearchTO);

    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);
}
