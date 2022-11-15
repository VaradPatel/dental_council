package in.gov.abdm.nmr.domain.common;

import java.sql.Timestamp;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import in.gov.abdm.nmr.domain.user_detail.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity {

    @CreatedDate
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "created_by")
    private UserDetail createdBy;

    @LastModifiedDate
    private Timestamp updatedAt;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private UserDetail updatedBy;
}
