package in.gov.abdm.nmr.domain.user_detail;

public interface IUserDetailController {

    UserDetailTO findByUsername(String username);
    
    String findRefreshTokenIdByUsername(String username);
    
    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);
}
