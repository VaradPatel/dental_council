package in.gov.abdm.nmr.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "otp")
@Table(name = "otp")
public class Otp {

    @Id
    private String id;
    @Column(name = "otp_hash")
    private String otpHash;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "expires_at")
    private Timestamp expiresAt;
    @Column(name = "expired")
    private Boolean expired;
    @Column(name = "attempts")
    private Integer attempts;
    @Column(name = "contact")
    private String contact;

    public Otp() {
        super();
    }

    public Otp(String id, String otpHash, Timestamp createdAt, Timestamp expiresAt, Boolean expired, Integer attempts,
               String contact) {
        super();
        this.id = id;
        this.otpHash = otpHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.expired = expired;
        this.attempts = attempts;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

