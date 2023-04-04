package in.gov.abdm.nmr.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FieldQueries extends CommonAuditEntity {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "hpProfile")
    private HpProfile hpProfile;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User login;

    private String fieldName;
    private String queryComment;
    private String queryBy;
    private String queryStatus;
    private String fieldLabel;
    private Integer queryType;
    private String sectionName;

}
