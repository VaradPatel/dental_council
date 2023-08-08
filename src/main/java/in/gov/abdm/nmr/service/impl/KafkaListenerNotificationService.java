package in.gov.abdm.nmr.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.FileESignedEventTO;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.ESignStatus;
import in.gov.abdm.nmr.exception.DateException;
import in.gov.abdm.nmr.repository.IAddressRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This is a service implementation class for Kafka listener notification service
 */
@Service
@Slf4j
public class KafkaListenerNotificationService {

    @Autowired
    IHpProfileRepository iHpProfileRepository;
    @Autowired
    IAddressRepository iAddressRepository;

    @Autowired
    INotificationService notificationService;

    /**
     * This method consumes a message from Kafka topic and updates the e-sign status of a health profile if the message contains valid details.
     * * @param eventMessage the message to be consumed from Kafka topic
     * * @throws JsonProcessingException if an error occurs while processing the JSON message
     */
    @KafkaListener(topics = "${spring.profiles.active}"  + NMRConstants.UNDERSCORE + NMRConstants.KAFKA_TOPIC, groupId = "${spring.profiles.active}" + NMRConstants.UNDERSCORE + NMRConstants.KAFKA_GROUP_ID)
    public void consume(String eventMessage) {
        try {
            log.info("council Kafka topic name :{} and group Id :{} Request received : {} ", NMRConstants.KAFKA_TOPIC, NMRConstants.KAFKA_GROUP_ID, eventMessage);
            ObjectMapper objectMapper = new ObjectMapper();
            FileESignedEventTO eSignedEvent = objectMapper.readValue(eventMessage, FileESignedEventTO.class);
            String transactionId = eSignedEvent.getTransactionId().substring(0, eSignedEvent.getTransactionId().lastIndexOf("."));
            HpProfile hpProfile = iHpProfileRepository.findByTransactionId(transactionId);
            if (hpProfile != null) {
                log.debug("Fetched hp profile detail successfully for hp profile ID: {}", hpProfile.getId());
                Address address = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfile.getId(), AddressType.KYC.getId());
                if (address != null) {
                    log.debug("Fetched address detail successfully for hp profile ID: {}", address.getHpProfileId());
                    if (hpProfile.getFullName().equalsIgnoreCase(eSignedEvent.getName()) &&
                            getBirthYear(hpProfile.getDateOfBirth().toString()) == Integer.parseInt(eSignedEvent.getYob()) &&
                            address.getPincode().equalsIgnoreCase(eSignedEvent.getPincode())) {
                        iHpProfileRepository.updateEsignStatus(hpProfile.getId(), ESignStatus.PROFILE_ESIGNED_WITH_SAME_AADHAR.getId());
                        try {
                            if(hpProfile.getRequestId()!=null && isRegistrationRequest(hpProfile.getRequestId())) {

                                if (hpProfile.getUser().isSmsNotificationEnabled() && hpProfile.getUser().isEmailNotificationEnabled()) {
                                    notificationService.sendNotificationOnStatusChangeForHP(ApplicationType.HP_REGISTRATION.getNotifyText(), Action.SUBMIT.getNotifyText(), hpProfile.getUser().getMobileNumber(), hpProfile.getUser().getEmail());
                                } else if (hpProfile.getUser().isSmsNotificationEnabled()) {
                                    notificationService.sendNotificationOnStatusChangeForHP(ApplicationType.HP_REGISTRATION.getNotifyText(), Action.SUBMIT.getNotifyText(), hpProfile.getUser().getMobileNumber(), null);
                                } else if (hpProfile.getUser().isEmailNotificationEnabled()) {
                                    notificationService.sendNotificationOnStatusChangeForHP(ApplicationType.HP_REGISTRATION.getNotifyText(), Action.SUBMIT.getNotifyText(), null, hpProfile.getUser().getEmail());
                                }
                            }
                        } catch (Exception exception) {
                            log.debug("error occurred while sending notification:" + exception.getLocalizedMessage());
                        }
                        log.info("updated e sign status:{} for Transaction ID: {}", ESignStatus.PROFILE_ESIGNED_WITH_SAME_AADHAR.getStatus(), transactionId);

                    } else {
                        iHpProfileRepository.updateEsignStatus(hpProfile.getId(), ESignStatus.PROFILE_ESIGNED_WITH_DIFFERENT_AADHAR.getId());
                        try {
                            notificationService.sendNotificationForIncorrectESign(hpProfile.getFullName(), hpProfile.getUser().getMobileNumber(), hpProfile.getUser().getEmail());
                        }
                        catch(Exception exception){
                            log.debug("error occurred while sending notification:" + exception.getLocalizedMessage());
                        }
                        log.info("updated e sign status:{} for Transaction ID: {}", ESignStatus.PROFILE_ESIGNED_WITH_DIFFERENT_AADHAR.getStatus(), transactionId);
                    }
                } else {
                    log.error("transaction id: {}, could not be found.", transactionId);
                }
            } else {
                log.error("transaction id: {}, could not be found.", transactionId);
            }
        } catch (Exception ex) {
            log.error("Error occurred while processing message: {}", eventMessage, ex);
        }
    }

    /**
     * Returns the birth year of a person given their date of birth.
     * <p>
     * The date of birth should be in the format "yyyy-MM-dd".
     *
     * @param dateOfBirth the date of birth of the person
     * @return the birth year of the person
     * @throws DateTimeParseException if the date of birth is in an invalid format
     */
    public static int getBirthYear(String dateOfBirth) throws DateTimeParseException, DateException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob;
        try {
            dob = LocalDate.parse(dateOfBirth, formatter);
        } catch (DateTimeParseException ex) {
            log.error("Error parsing date of birth: {}", dateOfBirth);
            throw new DateException();
        }
        return dob.getYear();
    }

    private boolean isRegistrationRequest(String requestId){
        String registrationRequestIdentifier="1";
        return registrationRequestIdentifier.equals(String.valueOf(requestId.charAt(3)));
    }
}