package in.gov.abdm.nmr.domain.user_detail;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailService implements IUserDetailService {

    private UserDetailRepository userDetailRepository;

    private UserDetailDtoMapper dtoEntityMapper;

    public UserDetailService(UserDetailRepository userDetailRepository, UserDetailDtoMapper dtoEntityMapper) {
        this.userDetailRepository = userDetailRepository;
        this.dtoEntityMapper = dtoEntityMapper;
    }

    @Override
    public UserDetailTO findByUsername(String username) {
        UserDetail userDetail = userDetailRepository.findByUsername(username).orElse(null);
        return dtoEntityMapper.userDetailToDto(userDetail);
    }

    @Override
    public String findRefreshTokenIdByUsername(String username) {
        return userDetailRepository.findRefreshTokenIdByUsername(username).orElse("");
    }

    @Override
    public Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO) {
        return userDetailRepository.updateRefreshTokenId(refreshTokenRequestTO.getUsername(), refreshTokenRequestTO.getRefreshTokenId()).orElse(0);
    }
}
