package in.gov.abdm.nmr.security.config;

import brave.Tracer;
import in.gov.abdm.nmr.jpa.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityAuditEventRepository implements AuditEventRepository {

    private ISecurityAuditTrailDaoService securityAuditTrailDaoService;
    
    private Tracer tracer;

    public SecurityAuditEventRepository(ISecurityAuditTrailDaoService securityAuditTrailDaoService, Tracer tracer) {
        this.securityAuditTrailDaoService = securityAuditTrailDaoService;
        this.tracer = tracer;
    }

    @Override
    public void add(AuditEvent event) {
        SecurityAuditTrail details = (SecurityAuditTrail) event.getData().get("details");
        details.setCreatedAt(Timestamp.from(event.getTimestamp()));
        details.setStatus(event.getType());
        details.setUsername(event.getPrincipal());
        details.setCorrelationId(tracer.currentSpan().context().traceIdString());
        securityAuditTrailDaoService.saveAndFlush(details);
    }

    @Override
    public List<AuditEvent> find(String principal, Instant after, String type) {
        List<AuditEvent> eventList = new ArrayList<>();
        for (SecurityAuditTrail securityAuditTrail : securityAuditTrailDaoService.findByUserAndCreatedAtGreaterThanEqualAndStatus(principal, Timestamp.from(after), type)) {
            Map<String, Object> data = new HashMap<>();
            data.put("details", securityAuditTrail);
            eventList.add(new AuditEvent(securityAuditTrail.getUsername(), securityAuditTrail.getStatus(), data));
        }
        return eventList;
    }
}
