package in.gov.abdm.nmr.api.ext.user_detail;

public interface IUserDetailService {

    UserDetailTO findByUsername(String username);

    String findRefreshTokenIdByUsername(String username);

    Integer updateRefreshTokenId(String username, String refreshTokenId);
}
