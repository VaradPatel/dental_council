package in.gov.abdm.nmr.db.sql.domain.user_type;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.db.sql.domain.common.CommonAuditEntity;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static in.gov.abdm.nmr.api.constant.NMRConstants.USER_TYPE;

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
    private String code;

    @OneToMany(mappedBy = USER_TYPE, fetch = FetchType.LAZY)
    private List<UserSubType> userSubTypes;
}