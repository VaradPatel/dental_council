package in.gov.abdm.nmr.controller;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class VerifyEmailController {

    private IUserService userService;

    public VerifyEmailController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * marks email as verified when clicked on link
     * @param token unique identifier
     * @return html page for success and failure
     */
    @GetMapping(NMRConstants.VERIFY_EMAIL)
    public String verifyEmail(@RequestParam("token") String token ) {
        return userService.verifyEmail(token);
    }
}
