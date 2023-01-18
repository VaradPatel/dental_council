package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.stereotype.Service;
import in.gov.abdm.nmr.dto.NotificationToggleResponseTO;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.mapper.INbeMapper;
import in.gov.abdm.nmr.mapper.INmcMapper;
import in.gov.abdm.nmr.mapper.ISmcMapper;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IUserService;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigInteger;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private ISmcMapper smcMapper;
    @Autowired
    private INmcMapper nmcMapper;
    @Autowired
    private INbeMapper nbeMapper;

    private IUserDaoService userDaoService;

    public UserServiceImpl(IUserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override
    public NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled) {
        User userDetail = userDaoService.toggleSmsNotification(isSmsNotificationEnabled);
        return new NotificationToggleResponseTO(userDetail.getId(), NMRConstants.SMS, userDetail.isSmsNotificationEnabled());
    }

    @Override
    public NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled) {
        User userDetail = userDaoService.toggleEmailNotification(isEmailNotificationEnabled);
        return new NotificationToggleResponseTO(userDetail.getId(), NMRConstants.EMAIL, userDetail.isEmailNotificationEnabled());
    }

    @Override
    public List<NotificationToggleResponseTO> toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO) {
        User userDetail = userDaoService.toggleNotification(notificationToggleRequestTO);
        return List.of(NotificationToggleResponseTO.builder().userId(userDetail.getId()).mode(NMRConstants.SMS).enabled(userDetail.isSmsNotificationEnabled()).build(),
                NotificationToggleResponseTO.builder().userId(userDetail.getId()).mode(NMRConstants.EMAIL).enabled(userDetail.isEmailNotificationEnabled()).build());
    }

    /**
     * Increases login failed attempt by 1
     *
     * @param user user class object
     */
    public void increaseFailedAttempts(User user) {
        user.setFailedAttempt(user.getFailedAttempt() + 1);
        userDaoService.saveUserDetail(user);
    }

    /**
     * Sets user status to lock and create entry for lock time
     *
     * @param user class object
     */
    public void lock(User user) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        user.setAccountNonLocked(false);
        user.setLockTime(new Timestamp(calendar.getTimeInMillis()));
        userDaoService.saveUserDetail(user);
    }

    /**
     * Unlocked user after expiry time
     *
     * @param user class object
     * @return success/failure of account unlocking
     */
    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + NMRConstants.LOCK_TIME_DURATION * 60 * 60 * 1000 < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userDaoService.saveUserDetail(user);
            return true;
        }
        return false;
    }
    @Override
    public SMCProfileTO getSmcProfile(BigInteger userId) {
        return smcMapper.smcProfileToDto(userDaoService.findSmcProfileByUserId(userId));
    }

    @Override
    public NmcProfileTO getNmcProfile(BigInteger userId) {
        return nmcMapper.nmcProfileToDto(userDaoService.findNmcProfileByUserId(userId));
    }

    @Override
    public NbeProfileTO getNbeProfile(BigInteger userId) {
         return nbeMapper.nbeProfileToDto( userDaoService.findNbeProfileByUserId(userId));
    }

}
