package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PutMapping(path = ProtectedPaths.PATH_USER_NOTIFICATION_ENABLED, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NotificationToggleResponseTO> toggleNotification(@RequestBody NotificationToggleRequestTO notificationToggleRequestTO) {
        return userService.toggleNotification(notificationToggleRequestTO);
    }

    @GetMapping(path = ProtectedPaths.PATH_SMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    //---
    @RolesAllowed({RoleConstants.STATE_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public SMCProfileTO smcProfile(@PathVariable(name = "id") BigInteger id) throws NmrException, InvalidIdException {
        return userService.getSmcProfile(id);
    }

    @GetMapping(path = ProtectedPaths.PATH_NMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    //---
    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public NmcProfileTO nmcProfile(@PathVariable(name = "id") BigInteger id) throws NmrException, InvalidIdException {
        return userService.getNmcProfile(id);
    }

    @GetMapping(path = ProtectedPaths.PATH_NBE_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    //---
    @RolesAllowed({RoleConstants.NBE})
    @SecurityRequirement(name = "bearerAuth")
    public NbeProfileTO nbeProfile(@PathVariable(name = "id") BigInteger id) throws NmrException, InvalidIdException {
        return userService.getNbeProfile(id);
    }

    @PutMapping(path = ProtectedPaths.PATH_SMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.STATE_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public SMCProfileTO updateSMCProfile(@PathVariable(name = "id") BigInteger id, @RequestBody SMCProfileTO smcProfileTO) throws NmrException, InvalidIdException {
        return userService.updateSmcProfile(id, smcProfileTO);
    }

    @PutMapping(path = ProtectedPaths.PATH_NMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public NmcProfileTO updateNmcProfile(@PathVariable(name = "id") BigInteger id, @RequestBody NmcProfileTO nmcProfileTO) throws NmrException, InvalidIdException {
        return userService.updateNmcProfile(id, nmcProfileTO);
    }

    @PutMapping(path = ProtectedPaths.PATH_NBE_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NBE})
    @SecurityRequirement(name = "bearerAuth")
    public NbeProfileTO updateNbeProfile(@PathVariable(name = "id") BigInteger id, @RequestBody NbeProfileTO nbeProfileTO) throws NmrException, InvalidIdException {
        return userService.updateNbeProfile(id, nbeProfileTO);
    }

    @PostMapping(path = NMRConstants.PATH_HP_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo createHpUserAccount(@RequestBody CreateHpUserAccountTo createHpUserAccountTo){
        return userService.createHpUserAccount(createHpUserAccountTo);
    }

    @PostMapping(path = NMRConstants.RETRIEVE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public String retrieveUser(@Valid @RequestBody RetrieveUserRequestTo retrieveUserRequestTo) throws OtpException {
        return userService.retrieveUser(retrieveUserRequestTo);
    }


    /**
     * mark email id as verified
     * @param verifyEmailTo receiver email/mobile
     * @return Success/Fail message
     */
    @PostMapping(NMRConstants.VERIFY_EMAIL)
    public ResponseMessageTo verifyEmail(@RequestBody VerifyEmailTo verifyEmailTo) {

        return userService.verifyEmail(verifyEmailTo);

    }
}
