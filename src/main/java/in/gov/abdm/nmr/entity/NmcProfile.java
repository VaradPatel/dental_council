package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.entity.CommonAuditEntity;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "nmcProfile")
public class NmcProfile extends CommonAuditEntity {

    @Id
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = "userDetail")
    private User userDetail;


    private String firstName;
    private String lastName;
    private String middleName;

    @OneToOne
    private StateMedicalCouncil stateMedicalCouncil;

    private Integer ndhmEnrollment;
    private Integer enrolledNumber;
    private String displayName;
}
