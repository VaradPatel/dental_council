package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.dto.NotificationToggleResponseTO;
import in.gov.abdm.nmr.entity.User;
import java.util.List;

public interface IUserService {

    NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled);

    NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled);

    List<NotificationToggleResponseTO> toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

    void increaseFailedAttempts(User user);

    void lock(User user);

    boolean unlockWhenTimeExpired(User user);
}
