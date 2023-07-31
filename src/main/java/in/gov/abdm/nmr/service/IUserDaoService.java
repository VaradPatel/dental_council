package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;

import java.math.BigInteger;
import java.util.List;

public interface IUserDaoService {

    Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO);

    User findById(BigInteger id);

    User save(User user);

    User findByUsername(String username, BigInteger userType);

    User findByMobileNumberAndUserTypeId(String mobileNumber, BigInteger userType);

    boolean existsByUserNameAndUserTypeId(String userName, BigInteger userType);

    boolean existsByMobileNumberAndUserTypeId(String mobileNumber, BigInteger userType);
    boolean existsByEmailAndUserTypeId(String email, BigInteger userType);

    User toggleSmsNotification(boolean isSmsNotificationEnabled);

    User toggleEmailNotification(boolean isEmailNotificationEnabled);

    User toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO);

    SMCProfile findSmcProfile(BigInteger id) throws NmrException, InvalidIdException;

    NmcProfile findNmcProfile(BigInteger id) throws NmrException, InvalidIdException;

    NbeProfile findNbeProfile(BigInteger id) throws NmrException, InvalidIdException;

    SMCProfile updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException, InvalidIdException, InvalidRequestException;

    NmcProfile updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException, InvalidIdException, InvalidRequestException;

    NbeProfile updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException, InvalidIdException, InvalidRequestException;

    boolean checkEmailUsedByOtherUser(BigInteger id, String email, BigInteger userType);

    boolean checkMobileUsedByOtherUser(BigInteger id, String mobile, BigInteger userType);

    void unlockUser(BigInteger userId);

    void updateLastLogin(BigInteger userId);

    List<String> getUserNames(String mobileNumber, BigInteger userType);
}
