package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "userType")
public class UserType extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;

    @OneToMany(mappedBy = USER_TYPE, fetch = FetchType.LAZY)
    private List<UserSubType> userSubTypes;

    @OneToOne
    @JoinColumn(name = GROUP_ID_COLUMN,referencedColumnName = ID)
    private UserGroup group;
    
    private String roles;
}