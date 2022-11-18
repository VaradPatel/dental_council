package in.gov.abdm.nmr.domain.registration_detail;

import java.sql.Timestamp;
import java.util.Date;

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
public class RegistrationDetail extends CommonAuditEntity {

    @Id
    private Long id;
    private String systemOfMedicine;
    private Long hpProfileId;
    private String councilName;
    private String councilRegistrationNo;
    private String certificate;
    private Date registrationDate;
    private Date registrationFromDate;
    private Date registrationToDate;
    private Date registrationDateOld;
    private String whetherRegisteredWithOthers;
    private Boolean isNameChange;
    private String nameChangeProof;
    private Long verifyBy;
    private Timestamp verifiedTime;
    private String isRenewable;
    private Date renewableRegistrationDate;
    private Date renewableRegistrationFromDate;
    private Date renewableRegistrationToDate;
    private Long isNuid;
    private Long nuidNumber;
    private Date nuidValidTill;
}
