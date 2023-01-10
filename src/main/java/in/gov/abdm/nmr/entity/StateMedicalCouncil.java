package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Entity(name = "stateMedicalCouncil")
public class StateMedicalCouncil extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String code;
    private String name;
    private String nameShort;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "state")
    private String state;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
