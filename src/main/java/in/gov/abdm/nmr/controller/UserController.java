package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IUserService;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    
    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PutMapping(path = ProtectedPaths.PATH_USER_SMSNOTIFICATIONENABLED, consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationToggleResponseTO toggleSmsNotification(@RequestBody boolean isSmsNotificationEnabled) {
        return userService.toggleSmsNotification(isSmsNotificationEnabled);
    }
    
    @PutMapping(path = ProtectedPaths.PATH_USER_EMAILNOTIFICATIONENABLED, consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationToggleResponseTO toggleEmailNotification(@RequestBody boolean isEmailNotificationEnabled) {
        return userService.toggleEmailNotification(isEmailNotificationEnabled);
    }

    @PutMapping(path = ProtectedPaths.PATH_USER_NOTIFICATION_ENABLED, consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NotificationToggleResponseTO> toggleNotification(@RequestBody NotificationToggleRequestTO notificationToggleRequestTO) {
        return userService.toggleNotification(notificationToggleRequestTO);
    }
    @GetMapping(path="/smc/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public SMCProfileTO smcProfile(@PathVariable(name = "id") BigInteger userId){
        return userService.getSmcProfile(userId);
    }
    @GetMapping(path="/nmc/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public NmcProfileTO nmcProfile(@PathVariable(name = "id") BigInteger userId){
        return userService.getNmcProfile(userId);
    }
  @GetMapping(path="/nbe/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public NbeProfileTO nbeProfile(@PathVariable(name = "id") BigInteger userId){
      return userService.getNbeProfile(userId);
  }
}
