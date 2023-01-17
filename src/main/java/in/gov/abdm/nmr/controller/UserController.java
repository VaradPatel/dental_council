package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.NotificationToggleResponseTO;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IUserService;

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

}
