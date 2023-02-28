package in.gov.abdm.nmr.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import in.gov.abdm.nmr.redis.hash.Otp;

@Repository
public interface IOtpDaoService {

    Otp save(Otp otp);
    
    Otp findById(String id);

    void deleteById(String id);

    List<Otp> findAllByContact(String contact);
}
