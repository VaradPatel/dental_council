package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "userSubType")
public class UserSubType extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;

    @ManyToOne
    @JoinColumn(name = USER_TYPE_ID,referencedColumnName = ID)
    private UserType userType;

    @OneToOne
    @JoinColumn(name = GROUP_ID_COLUMN,referencedColumnName = ID)
    private Group group;
}
