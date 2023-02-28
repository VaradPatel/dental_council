package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.redis.hash.Captcha;

public interface ICaptchaDaoService {

    Captcha save(Captcha captcha);

    void deleteById(String id);

    Captcha findById(String id);
}
