package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.INbeMapper;
import in.gov.abdm.nmr.mapper.INmcMapper;
import in.gov.abdm.nmr.mapper.ISmcMapper;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
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

    @Autowired
    private EntityManager entityManager;

    @Autowired
    RsaUtil rsaUtil;

    @Autowired
    IHpProfileRepository hpProfileRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @Override
    public SMCProfileTO updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException {
        return smcMapper.smcProfileToDto(userDaoService.updateSmcProfile(id, smcProfileTO));
    }

    @Override
    public NmcProfileTO updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException {
        return nmcMapper.nmcProfileToDto(userDaoService.updateNmcProfile(id, nmcProfileTO));
    }

    @Override
    public NbeProfileTO updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException {
        return nbeMapper.nbeProfileToDto(userDaoService.updateNbeProfile(id, nbeProfileTO));
    }

    @Override
    public ResponseMessageTo createHpUserAccount(CreateHpUserAccountTo createHpUserAccountTo) {
        try {
            
            if (!userDaoService.existsByUsername(createHpUserAccountTo.getUsername())) {
                User userDetail = new User(null, createHpUserAccountTo.getEmail(), createHpUserAccountTo.getMobile(), createHpUserAccountTo.getUsername(), null, bCryptPasswordEncoder.encode(rsaUtil.decrypt(createHpUserAccountTo.getPassword())), null, true, true, //
                        entityManager.getReference(UserType.class, UserTypeEnum.HEALTH_PROFESSIONAL.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserGroup.class, Group.HEALTH_PROFESSIONAL.getId()), true, 0, null);
                userDaoService.save(userDetail);

                List<HpProfile> hpProfileList=hpProfileRepository.findHpProfileByRegistrationId(createHpUserAccountTo.getRegistrationNumber());
                List<HpProfile> hpProfiles= new ArrayList<>();
                hpProfileList.forEach(hpProfile -> {
                    hpProfile.setUser(userDetail);
                    if(createHpUserAccountTo.getMobile() != null) {
                        hpProfile.setMobileNumber(createHpUserAccountTo.getMobile());
                    }
                    hpProfiles.add(hpProfile);
                });
                hpProfileRepository.saveAll(hpProfiles);
                return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
            } else {
                return new ResponseMessageTo(NMRConstants.USER_ALREADY_EXISTS);
            }
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }

}
