package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.NotificationToggleResponseTO;

public interface IUserService {

    NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled);

    NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled);
}
