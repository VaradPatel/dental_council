package in.gov.abdm.nmr.db.sql.domain.state_medical_council;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    @JoinColumn(name = "userDetail")
    private User userDetail;
}
