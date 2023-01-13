package in.gov.abdm.nmr.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "securityAuditTrail")
public class SecurityAuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String processId;
    private String ipAddress;
    private String userAgent;
    private String httpMethod;
    private String endpoint;
    private String username;
    private String status;
    private String correlationId;
    private Timestamp createdAt;
}
