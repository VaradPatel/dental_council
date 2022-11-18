package in.gov.abdm.nmr.domain.hp_profile;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import in.gov.abdm.nmr.domain.common.CommonAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HpProfile extends CommonAuditEntity {

    @Id
    private Long id;
    private String nmrId;
    private String salutation;
    private String fullName;
    private String emailOfficial;
    private String panNumber;
    private String panName;
    private String primaryContactNo;
    private Integer mobileNumber;
    private Date dateOfBirth;
    private String profilePhoto;
    private String gender;
    private String middleName;
    private String lastName;
    private String maritalStatus;
    private String spouseName;
    private String changedName;
    private String professionalType;
    private String language;
    private String picName;
    private String signatureName;
    private String systemOfMedicine;
    private Long addressId;
}
