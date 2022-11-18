package in.gov.abdm.nmr.api.ext.service;

import in.gov.abdm.nmr.api.ext.dto.UserDetailTO;

public interface IUserDetailService {

    UserDetailTO findByUsername(String username);
    
    String findRefreshTokenIdByIdAndRefreshTokenId(Long id, String refreshTokenId);
    
    Integer updateRefreshTokenId(Long id, String refreshTokenId);
}
