package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.service.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PutMapping(path = ProtectedPaths.PATH_USER_SMSNOTIFICATIONENABLED, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationToggleResponseTO toggleSmsNotification(@RequestBody boolean isSmsNotificationEnabled) {
        return userService.toggleSmsNotification(isSmsNotificationEnabled);
    }

    @PutMapping(path = ProtectedPaths.PATH_USER_EMAILNOTIFICATIONENABLED, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationToggleResponseTO toggleEmailNotification(@RequestBody boolean isEmailNotificationEnabled) {
        return userService.toggleEmailNotification(isEmailNotificationEnabled);
    }

    @PutMapping(path = ProtectedPaths.PATH_USER_NOTIFICATION_ENABLED, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NotificationToggleResponseTO> toggleNotification(@RequestBody NotificationToggleRequestTO notificationToggleRequestTO) {
        return userService.toggleNotification(notificationToggleRequestTO);
    }

    @GetMapping(path = ProtectedPaths.PATH_SMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    //---
    @RolesAllowed({RoleConstants.STATE_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public SMCProfileTO smcProfile(@PathVariable(name = "id") BigInteger id) throws NmrException {
        return userService.getSmcProfile(id);
    }

    @GetMapping(path = ProtectedPaths.PATH_NMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    //---
    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public NmcProfileTO nmcProfile(@PathVariable(name = "id") BigInteger id) throws NmrException {
        return userService.getNmcProfile(id);
    }

    @GetMapping(path = ProtectedPaths.PATH_NBE_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    //---
    @RolesAllowed({RoleConstants.NBE})
    @SecurityRequirement(name = "bearerAuth")
    public NbeProfileTO nbeProfile(@PathVariable(name = "id") BigInteger id) throws NmrException {
        return userService.getNbeProfile(id);
    }

    @PutMapping(path = ProtectedPaths.PATH_SMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.STATE_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public SMCProfileTO updateSMCProfile(@PathVariable(name = "id") BigInteger id, @RequestBody SMCProfileTO smcProfileTO) throws NmrException {
        return userService.updateSmcProfile(id, smcProfileTO);
    }

    @PutMapping(path = ProtectedPaths.PATH_NMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public NmcProfileTO updateNmcProfile(@PathVariable(name = "id") BigInteger id, @RequestBody NmcProfileTO nmcProfileTO) throws NmrException {
        return userService.updateNmcProfile(id, nmcProfileTO);
    }

    @PutMapping(path = ProtectedPaths.PATH_NBE_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NBE})
    @SecurityRequirement(name = "bearerAuth")
    public NbeProfileTO updateNbeProfile(@PathVariable(name = "id") BigInteger id, @RequestBody NbeProfileTO nbeProfileTO) throws NmrException {
        return userService.updateNbeProfile(id, nbeProfileTO);
    }
}
