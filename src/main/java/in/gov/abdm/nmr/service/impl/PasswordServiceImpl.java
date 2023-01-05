package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.ChangePasswordRequestTo;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementations of methods for resetting and changing password
 */
@Service
@Transactional
public class PasswordServiceImpl implements IPasswordService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Changes password related to username
     *
     * @param resetPasswordRequestTo coming from Service
     * @return ResetPasswordResponseTo Object
     */
    @Override
    public ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) {

        User user = userRepository.findByUsername(resetPasswordRequestTo.getUsername());

        if (null != user) {
            user.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequestTo.getPassword()));
            try {
                userRepository.save(user);
                return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
            } catch (Exception e) {
                return new ResponseMessageTo(NMRConstants.PROBLEM_OCCURRED);
            }
        } else {
            return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
        }

    }

    /**
     * After confirming old password, new password is created
     * @param changePasswordRequestTo coming from controller
     * @return Success or failure message
     */
    @Override
    public ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo) {

        User user = userRepository.findByUsername(changePasswordRequestTo.getUsername());

        if (null != user) {
            if (bCryptPasswordEncoder.matches(changePasswordRequestTo.getOldPassword(), user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(changePasswordRequestTo.getNewPassword()));
                try {
                    userRepository.save(user);
                    return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
                } catch (Exception e) {
                    return new ResponseMessageTo(NMRConstants.PROBLEM_OCCURRED);
                }
            } else {
                return new ResponseMessageTo(NMRConstants.OLD_PASSWORD_NOT_MATCHING);
            }
        } else {
            return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
        }

    }
}

