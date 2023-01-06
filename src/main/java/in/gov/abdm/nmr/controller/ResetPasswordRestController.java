package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.service.impl.ResetPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * Rest controller to perform reset password operations
 */
@RestController
public class ResetPasswordRestController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ResetPasswordService resetPasswordService;

    /**
     * Sends reset password link on email
     * @param email receiver email address
     * @return Success/Fail message
     */
    @PostMapping(NMRConstants.GET_RESET_PASSWORD_EMAIL)
    public ResponseMessageTo getResetPasswordEmail(@RequestParam String email) {

        String token = RandomString.make(30);

        try {
            if (resetPasswordService.createPasswordResetTokenForUser(email, token)) {
                String resetPasswordLink = resetPasswordService.getSiteURL(httpServletRequest) + NMRConstants.LOAD_RESET_PASSWORD_PAGE + "?token=" + token;
                resetPasswordService.sendEmail(email, resetPasswordLink);
            } else {
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseMessageTo(NMRConstants.FAILURE_RESPONSE);
        }
        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
    }
}