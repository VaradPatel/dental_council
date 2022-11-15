package in.gov.abdm.nmr.domain.user_detail;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import in.gov.abdm.nmr.domain.common.AuditEntity;
import in.gov.abdm.nmr.domain.password.Password;
import in.gov.abdm.nmr.domain.user_sub_type.UserSubType;
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
public class UserDetail extends AuditEntity {

    @Id
    private Long id;
    private String userName;

    @OneToOne
    @JoinColumn(name = "password")
    private Password password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type")
    private UserType userType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sub_type")
    private UserSubType userSubType;
}