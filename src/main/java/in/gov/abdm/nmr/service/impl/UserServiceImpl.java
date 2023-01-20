package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.INbeMapper;
import in.gov.abdm.nmr.mapper.INmcMapper;
import in.gov.abdm.nmr.mapper.ISmcMapper;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public SMCProfileTO getSmcProfile(BigInteger id) throws NmrException {
        return smcMapper.smcProfileToDto(userDaoService.findSmcProfile(id));
    }

    @Override
    public NmcProfileTO getNmcProfile(BigInteger id) throws NmrException {
        return nmcMapper.nmcProfileToDto(userDaoService.findNmcProfile(id));
    }

    @Override
    public NbeProfileTO getNbeProfile(BigInteger id) throws NmrException {
        return nbeMapper.nbeProfileToDto(userDaoService.findNbeProfile(id));
    }

}
