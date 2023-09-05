package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.service.IOtpDaoService;
import in.gov.abdm.nmr.service.IOtpValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OtpValidationServiceImpl implements IOtpValidationService {

    @Value("${nmr.otp.enabled}")
    private boolean otpEnabled;

    @Autowired
    private IOtpDaoService otpDaoService;

    @Override
    public boolean isOtpVerified(String id) {
        if (!otpEnabled) {
            return true;
        }

        Otp otp = otpDaoService.findById(id);
        if (otp == null) {
            return false;
        }
        otpDaoService.deleteById(id);
        return otp.isExpired();
    }

    @Override
    public boolean isOtpEnabled() {
        return otpEnabled;
    }
}
