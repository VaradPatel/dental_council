package in.gov.abdm.nmr.util;
import in.gov.abdm.audit.service.AuditServiceImpl;
import in.gov.abdm.event.AuditLogEvent;
import in.gov.abdm.nmr.entity.HpProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

/**
 * Util class for NMR.
 */

@Service
public class AuditLogPublisher {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.profiles.active}")
    String activeProfile;

    @Async
    public void publishHpProfileAuditLog(HpProfile hpProfile){

        AuditLogEvent auditLogEvent=new AuditLogEvent();
        auditLogEvent.setTableName("hp_profile");
        auditLogEvent.setRequestId(UUID.randomUUID().toString());
        auditLogEvent.setTimestamp(LocalDateTime.now().toString());
        HashMap<String,String> identifiers=new HashMap<>();
        identifiers.put("user_id",hpProfile.getUser().getId().toString());
        identifiers.put("hp_profile_id",hpProfile.getId().toString());
        auditLogEvent.setRowIdentifiers(identifiers);
        auditLogEvent.setRowContent(hpProfile.toString());
        auditLogEvent.setApplicationName("NMR");
        auditLogEvent.setEnvironment(activeProfile);

        publishAuditEvent(auditLogEvent);

    }

    public void publishAuditEvent(AuditLogEvent apiEvent){

        AuditServiceImpl auditService=new AuditServiceImpl();
        auditService.publishAuditLogEvent(kafkaTemplate,apiEvent);
    }
}
