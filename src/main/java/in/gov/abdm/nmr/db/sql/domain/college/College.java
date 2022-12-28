package in.gov.abdm.nmr.db.sql.domain.college;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.state.State;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncil;
import in.gov.abdm.nmr.db.sql.domain.university.University;
import in.gov.abdm.nmr.db.sql.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "colleges")
@Entity(name = "college")
public class College extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;

    private String collegeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university")
    private University university;
    private String phoneNumber;
    private String emailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateMedicalCouncil")
    private StateMedicalCouncil stateMedicalCouncil;
    private String website;
    private String address;
    private String pinCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state")
    private State state;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
