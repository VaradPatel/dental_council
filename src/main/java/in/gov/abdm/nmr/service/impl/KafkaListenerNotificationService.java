package in.gov.abdm.nmr.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.FileESignedEventTO;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.repository.IAddressRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;

@Service
@Slf4j
public class KafkaListenerNotificationService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    IHpProfileRepository iHpProfileRepository;
    @Autowired
    IAddressRepository iAddressRepository;

    @KafkaListener(topics = "esign_topic", groupId = "esign_group_id")
    public void consume(String eventMessage) throws JsonProcessingException, InvalidRequestException {
        try {
            FileESignedEventTO eSignedEvent = objectMapper.readValue(eventMessage, FileESignedEventTO.class);
            String transactionId = eSignedEvent.getTransactionId().substring(0, eSignedEvent.getTransactionId().lastIndexOf("."));
            log.info("council Kafka topic name :{} Request received for transaction ID: {} ", "esign_topic", transactionId);
            HpProfile hpProfile = iHpProfileRepository.findByTransactionId(transactionId);
            if (hpProfile != null) {
                log.debug("Fetched hp profile detail successfully for hp profile ID: {}", hpProfile.getId());
                Address address = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfile.getId(), AddressType.COMMUNICATION.getId());
                if (address != null) {
                    log.debug("Fetched address detail successfully for hp profile ID: {}", address.getHpProfileId());
                    if (hpProfile.getFullName().equalsIgnoreCase(eSignedEvent.getName()) &&
                            getBirthYear(hpProfile.getDateOfBirth().toString()) == Integer.parseInt(eSignedEvent.getYob()) &&
                            address.getPincode().equalsIgnoreCase(eSignedEvent.getPincode())) {
                        iHpProfileRepository.updateEsignStatus(hpProfile.getId(), NMRConstants.E_SIGN_SUCCESS_STATUS);
                        log.info("updated e sign status:{} for Transaction ID: {}", NMRConstants.E_SIGN_SUCCESS_STATUS, transactionId);
                    } else {
                        iHpProfileRepository.updateEsignStatus(hpProfile.getId(), NMRConstants.E_SIGN_FAILURE_STATUS);
                        log.info("updated e sign status:{} for Transaction ID: {}", NMRConstants.E_SIGN_FAILURE_STATUS, transactionId);
                    }
                } else {
                    log.error("Invalid request input data transaction id: {}, was not matched error Council Address Data.", transactionId);
                    throw new InvalidRequestException("given input data was not matched error Council Data.");
                }
            } else {
                log.error("Invalid request input data transaction id: {}, was not matched error Council hp profile Data.", transactionId);
                throw new InvalidRequestException("given input data was not matched error Council Data.");
            }
        } catch (Exception ex) {
            log.error("Error occurred while processing message: {}", eventMessage, ex);
            throw ex;
        }
    }

    public static int getBirthYear(String dateOfBirth) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob;
        try {
            dob = LocalDate.parse(dateOfBirth, formatter);
        } catch (DateTimeParseException ex) {
            log.error("Error parsing date of birth: {}", dateOfBirth);
            throw new DateTimeParseException("Invalid date format", dateOfBirth, 0, ex);
        }
        return dob.getYear();
    }

}