package in.gov.abdm.nmr.service.impl;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResetPasswordResponseTo;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.IResetPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementations of methods to send and validate Aadhaar OTP
 */
@Service
@Transactional
public class ResetPasswordServiceImpl implements IResetPasswordService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Changes password related to username
     * @param resetPasswordRequestTo coming from Service
     * @return ResetPasswordResponseTo Object
     */
    @Override
    public ResetPasswordResponseTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) {

        User user = userRepository.findByUsername(resetPasswordRequestTo.getUsername());

        if (null != user) {
            user.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequestTo.getPassword()));
            try {
                userRepository.save(user);
                return new ResetPasswordResponseTo(NMRConstants.SUCCESS_RESPONSE);
            } catch (Exception e) {
                return new ResetPasswordResponseTo(NMRConstants.PROBLEM_OCCURRED);
            }
        } else {
            return new ResetPasswordResponseTo(NMRConstants.USER_NOT_FOUND);
        }

    }
}

