package in.gov.abdm.nmr.security.config;

import brave.Tracer;
import in.gov.abdm.nmr.jpa.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class SecurityAuditConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    @EventListener
    public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {
        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
        SecurityAuditTrail details = (SecurityAuditTrail) auditEvent.getData().get("details");
        LOGGER.debug(" Process Id: {} || IP Address: {} || User Agent: {} || User: {} || Method: {} || Endpoint: {} || Status: {}", //
                details.getProcessId(), details.getIpAddress(), details.getUserAgent(), auditEvent.getPrincipal(), details.getHttpMethod(), //
                details.getEndpoint(), auditEvent.getType());
    }

    @Bean
    public SecurityAuditEventRepository auditEventRepository(ISecurityAuditTrailDaoService securityAuditTrailDaoService, Tracer tracer) {
        return new SecurityAuditEventRepository(securityAuditTrailDaoService, tracer);
    }
}
