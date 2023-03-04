package in.gov.abdm.nmr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.redis.repository.IOtpRepository;
import in.gov.abdm.nmr.service.IOtpDaoService;

@Repository
public class OtpDaoServiceImpl implements IOtpDaoService {
    
    @Autowired
    private IOtpRepository otpRepository;

    @Override
    public Otp save(Otp otp) {
        return otpRepository.save(otp);
    }
    
    @Override
    public Otp findById(String id) {
        return otpRepository.findById(id).orElse(null);
    }

    @Override
    public List<Otp> findAllByContact(String contact) {
        return otpRepository.findAllBycontact(contact);
    }

    @Override
    public void deleteById(String id) {
        otpRepository.deleteById(id);
    }
}
