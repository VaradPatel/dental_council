package in.gov.abdm.nmr.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.redis.hash.Captcha;
import in.gov.abdm.nmr.redis.repository.ICaptchaRepository;
import in.gov.abdm.nmr.service.ICaptchaDaoService;

@Service
@Transactional
public class CaptchaDaoServiceImpl implements ICaptchaDaoService {

    private ICaptchaRepository captchaRepository;
    
    public CaptchaDaoServiceImpl(ICaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    @Override
    public Captcha save(Captcha captcha) {
        return captchaRepository.save(captcha);
    }
    
    @Override
    public Captcha findById(String id) {
        return captchaRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(String transId) {
        captchaRepository.deleteById(transId);
    }
}
