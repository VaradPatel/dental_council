package in.gov.abdm.nmr.domain.user_detail;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/userDetails")
public class UserDetailController {

    private IUserDetailService userDetailService;

    public UserDetailController(IUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping(path = "/queryBy/username", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDetailTO findByUsername(@RequestBody String username) {
        return userDetailService.findByUsername(username);
    }

    @GetMapping(path = "/refreshTokenId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String findRefreshTokenIdByUsername(@RequestBody String username) {
        return userDetailService.findRefreshTokenIdByUsername(username);
    }

    @PutMapping(path = "/refreshTokenId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateRefreshTokenId(@RequestBody UpdateRefreshTokenIdRequestTO refreshTokenRequestTO) {
        return userDetailService.updateRefreshTokenId(refreshTokenRequestTO);
    }
}
