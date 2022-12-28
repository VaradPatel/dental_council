package in.gov.abdm.nmr.db.sql.domain.nmc_profile;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncil;
import in.gov.abdm.nmr.db.sql.domain.user_detail.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NMCProfile extends CommonAuditEntity {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "userDetail")
    private User user;


    private String firstName;
    private String lastName;
    private String middleName;

    @OneToOne
    private StateMedicalCouncil stateMedicalCouncil;

    private Integer ndhmEnrollment;
    private Integer enrolledNumber;
    private String displayName;
}
