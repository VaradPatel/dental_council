package in.gov.abdm.nmr.api.ext.service;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.ext.dto.UserDetailTO;

@Service
public class UserDetailService implements IUserDetailService {


    public UserDetailService() {
    }

    @Override
    public UserDetailTO findByUsername(String username) {
        return null;
    }

    @Override
    public String findRefreshTokenIdByIdAndRefreshTokenId(Long id, String refreshTokenId) {
        return null;
    }

    @Override
    public Integer updateRefreshTokenId(Long id, String refreshTokenId) {
        return null;
    }
}
