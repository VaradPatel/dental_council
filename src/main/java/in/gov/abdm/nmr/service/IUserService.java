package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.dto.NotificationToggleResponseTO;
import in.gov.abdm.nmr.dto.*;
import java.math.BigInteger;
import java.util.List;

public interface IUserService {

    NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled);

    NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled);

    List<NotificationToggleResponseTO> toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

   SMCProfileTO getSmcProfile(BigInteger userId);
   NmcProfileTO getNmcProfile(BigInteger userId);
   NbeProfileTO getNbeProfile(BigInteger userId);
}
