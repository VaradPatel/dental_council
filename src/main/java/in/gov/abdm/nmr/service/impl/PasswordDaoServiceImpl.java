package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.jpa.entity.Password;
import in.gov.abdm.nmr.jpa.repository.IPasswordRepository;
import in.gov.abdm.nmr.service.IPasswordDaoService;

@Service
@Transactional
public class PasswordDaoServiceImpl implements IPasswordDaoService {
    
    private IPasswordRepository passwordRepository;

    public PasswordDaoServiceImpl(IPasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    @Override
    public Password save(Password password) {
        return passwordRepository.save(password);
    }

    @Override
    public List<Password> findLast5(BigInteger userId) {
        return passwordRepository.findFirst5ByUserIdOrderByCreatedAtDesc(userId);
    }
}
