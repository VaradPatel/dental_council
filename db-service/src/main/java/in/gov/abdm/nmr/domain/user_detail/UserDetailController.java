package in.gov.abdm.nmr.domain.user_detail;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.domain.user_detail.to.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.domain.user_detail.to.UserDetailSearchTO;
import in.gov.abdm.nmr.domain.user_detail.to.UserDetailTO;

@RestController
@RequestMapping("/userDetails")
public class UserDetailController {

    private IUserDetailService userDetailService;

    public UserDetailController(IUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDetailTO searchUserDetail(@RequestBody UserDetailSearchTO userDetailSearchTO) {
        return userDetailService.searchUserDetail(userDetailSearchTO);
    }

    @GetMapping(path = "/refreshTokenId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String findRefreshTokenId(@RequestBody UserDetailSearchTO userDetailSearchTO) {
        return userDetailService.findRefreshTokenId(userDetailSearchTO);
    }

    @PatchMapping(path = "/refreshTokenId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateRefreshTokenId(@RequestBody UpdateRefreshTokenIdRequestTO refreshTokenRequestTO) {
        return userDetailService.updateRefreshTokenId(refreshTokenRequestTO);
    }
}
