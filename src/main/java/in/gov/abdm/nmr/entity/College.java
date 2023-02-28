package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

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
    private String requestId;
    
    private String pinCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private State state;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private boolean isApproved;
}
