package in.gov.abdm.nmr.controller;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.service.IResetPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller to reset password from mail link page
 */
@Controller
public class ResetPasswordController {

    @Autowired
    private IResetPasswordService resetPasswordService;

    /**
     * Loads reset password page
     * @param token unique token generated for transaction
     * @param model UI Model to return data on page
     * @return HTML Page for reset password
     */
    @GetMapping(NMRConstants.LOAD_RESET_PASSWORD_PAGE)
    public String showResetPasswordForm(@Param(value = NMRConstants.KEY_TOKEN) String token, Model model) {
        User user = resetPasswordService.getUserByPasswordResetToken(token);
        model.addAttribute(NMRConstants.KEY_TOKEN, token);
        if (user == null) {
            model.addAttribute(NMRConstants.KEY_MESSAGE, NMRConstants.INVALID_OR_EXPIRED_TOKEN);
            return NMRConstants.RESET_PASSWORD_FORM_NAME;
        }
        return NMRConstants.RESET_PASSWORD_FORM_NAME;
    }

    /**
     * Performs reset password operation
     * @param request coming from html page for reset password
     * @param model UI Model to return data on page
     * @return reset password page with success/failure
     */
    @PostMapping(NMRConstants.SET_NEW_PASSWORD)
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter(NMRConstants.KEY_TOKEN);
        String password = request.getParameter(NMRConstants.KEY_PASSWORD);

        User user = resetPasswordService.getUserByPasswordResetToken(token);

        if (user == null) {
            model.addAttribute(NMRConstants.KEY_MESSAGE, NMRConstants.INVALID_OR_EXPIRED_TOKEN);
            return NMRConstants.RESET_PASSWORD_FORM_NAME;
        } else {
            resetPasswordService.updatePassword(user, password);
            model.addAttribute(NMRConstants.KEY_MESSAGE, NMRConstants.PASSWORD_CHANGED_SUCCESSFULLY);
        }
        return NMRConstants.RESET_PASSWORD_FORM_NAME;
    }
}