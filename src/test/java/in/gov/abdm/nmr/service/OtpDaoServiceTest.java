package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.redis.repository.IOtpRepository;
import in.gov.abdm.nmr.service.impl.OtpDaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.MOBILE_NUMBER;
import static in.gov.abdm.nmr.util.CommonTestData.TRANSACTION_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpDaoServiceTest {

    @Mock
    IOtpRepository otpRepository;

    @InjectMocks
    OtpDaoServiceImpl otpDaoService;

    @Test
    void testSaveOtpShouldSaveOtp(){
        when(otpRepository.save(any(Otp.class))).thenReturn(getOtp());
        otpDaoService.save(getOtp());
        verify(otpRepository, times(1)).save(otpRepository.save(any(Otp.class)));

    }

    @Test
    void testFindByIdShouldReturnValidOtpDetails(){
        when(otpRepository.findById(anyString())).thenReturn(Optional.of(getOtp()));
        Otp otp = otpDaoService.findById(TRANSACTION_ID);
        assertEquals(TRANSACTION_ID, otp.getId());
    }

    @Test
    void testFindByIdShouldReturnNullWhenTransactionDetailsNotFound(){
        when(otpRepository.findById(anyString())).thenReturn(Optional.empty());
        assertNull(otpDaoService.findById(TRANSACTION_ID));
    }

    @Test
    void testFindAllByContactShouldReturnListOfOtp(){
        when(otpRepository.findAllBycontact(anyString())).thenReturn(List.of(getOtp()));
        List<Otp> otps = otpDaoService.findAllByContact(MOBILE_NUMBER);
        assertEquals(1, otps.size());
        assertEquals(TRANSACTION_ID, otps.get(0).getId());
    }

    @Test
    void testDeleteByIdShouldDeleteOtpFromDatabase(){
        doNothing().when(otpRepository).deleteById(anyString());
        otpDaoService.deleteById(TRANSACTION_ID);
        verify(otpRepository, times(1)).deleteById(anyString());
    }


    private Otp getOtp(){
        Otp otp = new Otp();
        otp.setOtp("1231454");
        otp.setAttempts(0);
        otp.setId(TRANSACTION_ID);
        otp.setContact(MOBILE_NUMBER);
        otp.setTimeToLive(15l);
        return otp;
    }
}
