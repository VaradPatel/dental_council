package in.gov.abdm.nmr.domain.user_sub_type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.domain.common.AuditEntity;
import in.gov.abdm.nmr.domain.user_type.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserSubType extends AuditEntity {

    @Id
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_type")
    private UserType userType;
}
