package in.gov.abdm.nmr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "smcProfile")
public class SMCProfile extends CommonAuditEntity {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
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
