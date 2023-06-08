package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.FileESignedEventTO;
import in.gov.abdm.nmr.repository.IAddressRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.service.impl.KafkaListenerNotificationService;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaListenerNotificationServiceTest {

    public static final String FILE_NAME_PDF = "FileName.pdf";
    @InjectMocks
    KafkaListenerNotificationService kafkaListenerNotificationService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    IHpProfileRepository iHpProfileRepository;
    @Mock
    IAddressRepository iAddressRepository;

    private static final String MESSAGE = """
                "transactionId":"FileName.pdf","lastAadharDigit":"1859","name":"John Doe","pincode":"123456","yob":"1990","signed":false
                """;

    @Test
    void testValidEsignedEventUpdateEsignStatusToSuccess() throws JsonProcessingException {
        FileESignedEventTO fileESignedEventTO = getFileESignedEventTO(PROFILE_DISPLAY_NAME, PIN_CODE);
        lenient().when(objectMapper.readValue(MESSAGE, FileESignedEventTO.class)).thenReturn(fileESignedEventTO);
        when(iHpProfileRepository.findByTransactionId(anyString())).thenReturn(CommonTestData.getHpProfile());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(any(BigInteger.class),anyInt())).thenReturn(CommonTestData.getCommunicationAddress());
        doNothing().when(iHpProfileRepository).updateEsignStatus(any(BigInteger.class), any(Integer.class));
        kafkaListenerNotificationService.consume(MESSAGE);
        verify(iHpProfileRepository, times(1)).updateEsignStatus(any(BigInteger.class), any(Integer.class));

    }

    @Test
    void testInValidEsignedEventUpdateEsignStatusToFailure() throws JsonProcessingException {
        FileESignedEventTO fileESignedEventTO = getFileESignedEventTO("John Foe", "123000");
        lenient().when(objectMapper.readValue(MESSAGE, FileESignedEventTO.class)).thenReturn(fileESignedEventTO);
        when(iHpProfileRepository.findByTransactionId(anyString())).thenReturn(CommonTestData.getHpProfile());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(any(BigInteger.class),anyInt())).thenReturn(CommonTestData.getCommunicationAddress());
        doNothing().when(iHpProfileRepository).updateEsignStatus(any(BigInteger.class), any(Integer.class));
        kafkaListenerNotificationService.consume(MESSAGE);
        verify(iHpProfileRepository, times(1)).updateEsignStatus(any(BigInteger.class), any(Integer.class));
    }

    @Test
    void testEsignedEventForIncorrectPayloaddShouldLogErrorWhenNoHpProfileFound() throws JsonProcessingException {
        FileESignedEventTO fileESignedEventTO = getFileESignedEventTO(PROFILE_DISPLAY_NAME, PIN_CODE);
        lenient().when(objectMapper.readValue(MESSAGE, FileESignedEventTO.class)).thenReturn(fileESignedEventTO);
        when(iHpProfileRepository.findByTransactionId(anyString())).thenReturn(null);
        kafkaListenerNotificationService.consume(MESSAGE);
        verify(iHpProfileRepository, times(0)).updateEsignStatus(any(BigInteger.class), any(Integer.class));
    }

    @Test
    void testEsignedEventForIncorrectPayloaddShouldLogErrorWhenNoHpProfileAddressFound() throws JsonProcessingException {
        FileESignedEventTO fileESignedEventTO = getFileESignedEventTO(PROFILE_DISPLAY_NAME, PIN_CODE);
        lenient().when(objectMapper.readValue(MESSAGE, FileESignedEventTO.class)).thenReturn(fileESignedEventTO);
        when(iHpProfileRepository.findByTransactionId(anyString())).thenReturn(getHpProfile());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(any(BigInteger.class),anyInt())).thenReturn(null);
        kafkaListenerNotificationService.consume(MESSAGE);
        verify(iHpProfileRepository, times(0)).updateEsignStatus(any(BigInteger.class), any(Integer.class));
    }

    @Test
    void testEsignedEventForInvalidPayload() throws JsonProcessingException {
        lenient().when(objectMapper.readValue(MESSAGE, FileESignedEventTO.class)).thenThrow(new RuntimeException());
        kafkaListenerNotificationService.consume(MESSAGE);
        verify(iHpProfileRepository, times(0)).updateEsignStatus(any(BigInteger.class), any(Integer.class));
    }

    private FileESignedEventTO getFileESignedEventTO(String profileDisplayName, String pinCode) {
        FileESignedEventTO fileESignedEventTO = new FileESignedEventTO();
        fileESignedEventTO.setIsSigned(true);
        fileESignedEventTO.setFileName("FileName");
        fileESignedEventTO.setName(profileDisplayName);
        fileESignedEventTO.setPincode(pinCode);
        fileESignedEventTO.setYob("1990");
        fileESignedEventTO.setTransactionId(FILE_NAME_PDF);
        return fileESignedEventTO;
    }

}
