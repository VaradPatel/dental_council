package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import in.gov.abdm.nmr.service.IAccessControlService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AccessControlServiceImpl implements IAccessControlService {

    private IUserRepository userRepository;

    public AccessControlServiceImpl(IUserRepository userDaoService) {
        this.userRepository = userDaoService;
    }

    @Override
    public void validateUser(BigInteger userId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();
        User loggedInUser = userRepository.findByUsername(userName, userType);
        if (loggedInUser == null || !loggedInUser.getId().equals(userId)) {
            throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
        }
    }

    @Override
    public User getLoggedInUser() {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();
        return userRepository.findByUsername(username, userType);
    }


}
