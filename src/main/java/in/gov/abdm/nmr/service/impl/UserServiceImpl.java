package in.gov.abdm.nmr.service.impl;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.dto.NotificationToggleResponseTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
    
    private IUserDaoService userDaoService;

    public UserServiceImpl(IUserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override
    public NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled) {
        User userDetail = userDaoService.toggleSmsNotification(isSmsNotificationEnabled);
        return new NotificationToggleResponseTO(userDetail.getId(), userDetail.isSmsNotificationEnabled());
    }

    @Override
    public NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled) {
        User userDetail = userDaoService.toggleEmailNotification(isEmailNotificationEnabled);
        return new NotificationToggleResponseTO(userDetail.getId(), userDetail.isEmailNotificationEnabled());
    }

}
