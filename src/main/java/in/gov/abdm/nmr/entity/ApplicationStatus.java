package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApplicationStatus extends CommonAuditEntity {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "hpProfile")
    private HpProfile hpProfile;

    @OneToOne
    private ApplicationStatusType applicationStatus;

    private String createdBy;
    private String updatedBy;
}
