package in.gov.abdm.nmr.domain.user_detail;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/userDetails")
public class UserDetailController implements IUserDetailController {

    private IUserDetailService userDetailService;

    public UserDetailController(IUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    @GetMapping(path = "/username", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDetailTO findByUsername(@RequestBody String username) {
        return userDetailService.findByUsername(username);
    }

    @Override
    @GetMapping(path = "/{id}/refreshTokenId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String findRefreshTokenIdByIdAndRefreshTokenId(@PathVariable Long id, @RequestBody String refreshTokenId) {
        return userDetailService.findRefreshTokenIdByIdAndRefreshTokenId(id, refreshTokenId);
    }

    @Override
    @PutMapping(path = "/{id}/refreshTokenId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateRefreshTokenId(@PathVariable Long id, @RequestBody String refreshTokenId) {
        return userDetailService.updateRefreshTokenId(id, refreshTokenId);
    }
}
