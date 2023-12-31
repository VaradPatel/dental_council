package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.security.ChecksumUtil;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Autowired
    ChecksumUtil checksumUtil;

    @PutMapping(path = ProtectedPaths.PATH_USER_NOTIFICATION_ENABLED, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NotificationToggleResponseTO> toggleNotification(@RequestBody NotificationToggleRequestTO notificationToggleRequestTO) {
        checksumUtil.validateChecksum();
        return userService.toggleNotification(notificationToggleRequestTO);
    }

    @GetMapping(path = ProtectedPaths.PATH_SMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.STATE_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public SMCProfileTO smcProfile(@PathVariable(name = "id") BigInteger id) throws NmrException, InvalidIdException {
        return userService.getSmcProfile(id);
    }

    @GetMapping(path = ProtectedPaths.PATH_NMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public NmcProfileTO nmcProfile(@PathVariable(name = "id") BigInteger id) throws NmrException, InvalidIdException {
        return userService.getNmcProfile(id);
    }

    @GetMapping(path = ProtectedPaths.PATH_NBE_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NATIONAL_BOARD_OF_EXAMINATIONS})
    @SecurityRequirement(name = "bearerAuth")
    public NbeProfileTO nbeProfile(@PathVariable(name = "id") BigInteger id) throws NmrException, InvalidIdException {
        return userService.getNbeProfile(id);
    }

    @PutMapping(path = ProtectedPaths.PATH_SMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.STATE_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public SMCProfileTO updateSMCProfile(@PathVariable(name = "id") BigInteger id, @RequestBody @Valid SMCProfileTO smcProfileTO) throws NmrException, InvalidIdException, InvalidRequestException, OtpException {
        checksumUtil.validateChecksum();
        return userService.updateSmcProfile(id, smcProfileTO);
    }

    @PutMapping(path = ProtectedPaths.PATH_NMC_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL})
    @SecurityRequirement(name = "bearerAuth")
    public NmcProfileTO updateNmcProfile(@PathVariable(name = "id") BigInteger id, @Valid @RequestBody NmcProfileTO nmcProfileTO) throws NmrException, InvalidIdException, InvalidRequestException,OtpException {
        checksumUtil.validateChecksum();
        return userService.updateNmcProfile(id, nmcProfileTO);
    }

    @PutMapping(path = ProtectedPaths.PATH_NBE_PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({RoleConstants.NATIONAL_BOARD_OF_EXAMINATIONS})
    @SecurityRequirement(name = "bearerAuth")
    public NbeProfileTO updateNbeProfile(@PathVariable(name = "id") BigInteger id, @Valid @RequestBody NbeProfileTO nbeProfileTO) throws NmrException, InvalidIdException, InvalidRequestException, OtpException {
        checksumUtil.validateChecksum();
        return userService.updateNbeProfile(id, nbeProfileTO);
    }

    @PostMapping(path = NMRConstants.RETRIEVE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public String retrieveUser(@Valid @RequestBody RetrieveUserRequestTo retrieveUserRequestTo) throws OtpException {
        checksumUtil.validateChecksum();
        return userService.retrieveUser(retrieveUserRequestTo);
    }

    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN})
    @PostMapping(path = ProtectedPaths.USER_NMC_CREATE_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfileTO createUser(@Valid @RequestBody UserProfileTO userProfileTO) throws NmrException {
        checksumUtil.validateChecksum();
        return userService.createUser(userProfileTO);
    }

    @GetMapping(path = "/user")
    public UserResponseTO retrieveUsers(
            @RequestParam(required = false, value = "search") String search,
            @RequestParam(required = false, value = "value") String value,
            @RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(required = false, value = "offset", defaultValue = "10") int offset,
            @RequestParam(required = false, value = "sortBy") String sortBy,
            @RequestParam(required = false, value = "sortOrder") String sortOrder) throws InvalidRequestException, AccessDeniedException {
        return userService.getAllUser(search, value, pageNo, offset, sortBy, sortOrder);
    }

    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN})
    @DeleteMapping(path = ProtectedPaths.USER_DEACTIVATE_URL)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseMessageTo deactivateUser(@PathVariable(name = "userId") BigInteger userId) {
        userService.deactivateUser(userId);
        return ResponseMessageTo.builder().message(NMRConstants.SUCCESS_RESPONSE).build();
    }

    @RolesAllowed({RoleConstants.NATIONAL_MEDICAL_COUNCIL_ADMIN})
    @PostMapping(path = ProtectedPaths.USER_UNLOCK_URL)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseMessageTo unlockUser(@PathVariable(name = "userId") BigInteger userId) {
        checksumUtil.validateChecksum();
        userService.unlockUser(userId);
        return ResponseMessageTo.builder().message(NMRConstants.SUCCESS_RESPONSE).build();
    }

    @GetMapping(path = "/user-accounts")
    public List<String> getUserNames(
            @RequestParam(value = "mobileNumber") String mobileNumber,
            @RequestParam(value = "userType") BigInteger userType) {
        return userService.getUserNames(mobileNumber, userType);
    }
}
