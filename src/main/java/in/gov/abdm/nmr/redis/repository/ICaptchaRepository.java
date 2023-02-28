package in.gov.abdm.nmr.redis.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

import in.gov.abdm.nmr.redis.hash.Captcha;

public interface ICaptchaRepository extends KeyValueRepository<Captcha, String> {

}
