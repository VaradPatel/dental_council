package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.NmrException;

import java.math.BigInteger;

public interface IUserDaoService {

    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);

    User findById(BigInteger id);

    User save(User user);

    User findByUsername(String username);

    User findFirstByMobileNumber(String mobileNumber);
    
    boolean existsByUsername(String username);

    boolean existsByUserName(String userName);

    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);

    User toggleSmsNotification(boolean isSmsNotificationEnabled);

    User toggleEmailNotification(boolean isEmailNotificationEnabled);

    User toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

    SMCProfile findSmcProfile(BigInteger id) throws NmrException, InvalidIdException;

    NmcProfile findNmcProfile(BigInteger id) throws NmrException, InvalidIdException;

    NbeProfile findNbeProfile(BigInteger id) throws NmrException, InvalidIdException;

    SMCProfile updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException, InvalidIdException;

    NmcProfile updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException, InvalidIdException;

    NbeProfile updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException, InvalidIdException;

    boolean checkEmailUsedByOtherUser(BigInteger id, String email);
}
