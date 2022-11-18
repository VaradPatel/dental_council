package in.gov.abdm.nmr.domain.user_detail;

public interface IUserDetailController {

    UserDetailTO findByUsername(String username);
    
    String findRefreshTokenIdByIdAndRefreshTokenId(Long id, String refreshTokenId);
    
    Integer updateRefreshTokenId(Long id, String refreshTokenId);
}
