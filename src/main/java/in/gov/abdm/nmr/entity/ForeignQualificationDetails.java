package in.gov.abdm.nmr.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;

import static in.gov.abdm.nmr.util.NMRConstants.ID;
import static in.gov.abdm.nmr.util.NMRConstants.STATE_MEDICAL_COUNCIL_ID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ForeignQualificationDetails extends CommonAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] certificate;
    private Date endDate;
    private Integer isNameChange;
    private Integer isVerified;
    private String name;
    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] nameChangeProofAttach;
    private String qualificationMonth;
    private String qualificationYear;
    private Date startDate;
    private String systemOfMedicine;
    private String requestId;
  
    private String district;
    private String college;
    private String country;
    private String course;
    private String state;
    private String university;

    @ManyToOne
    @JoinColumn(name = "registration_details_id", referencedColumnName = "id")
    private RegistrationDetails registrationDetails;

    @ManyToOne
    @JoinColumn(name = "hpProfileId", referencedColumnName = "id")
    private HpProfile hpProfile;
    @OneToOne
    @JoinColumn(name = "broad_speciality_id", referencedColumnName = "id")
    private BroadSpeciality broadSpeciality;
    private String superSpecialityName;
    private String fileName;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String NameChangeProofAttachFileName;
    @OneToOne
    @JoinColumn(name = STATE_MEDICAL_COUNCIL_ID,referencedColumnName = ID)
    private StateMedicalCouncil stateMedicalCouncil;

    private java.util.Date registrationDate;
    private String registrationNumber;
}
