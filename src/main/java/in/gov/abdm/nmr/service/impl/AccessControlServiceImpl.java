package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.IAccessControlService;

@Service
public class AccessControlServiceImpl implements IAccessControlService {

    private IUserRepository userRepository;

    public AccessControlServiceImpl(IUserRepository userDaoService) {
        this.userRepository = userDaoService;
    }

    @Override
    public void validateUser(BigInteger userId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findByUsername(userName);
        if (loggedInUser == null || !loggedInUser.getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }
    }
}