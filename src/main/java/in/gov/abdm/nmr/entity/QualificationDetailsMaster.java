package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class QualificationDetailsMaster extends CommonAuditEntity {

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

    @OneToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    private District district;

    @OneToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id")
    private CollegeMaster college;

    @OneToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @OneToOne
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private State state;

    @OneToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private UniversityMaster university;


    @ManyToOne
    @JoinColumn(name = "registration_details_id", referencedColumnName = "id")
    private RegistrationDetailsMaster registrationDetails;

    @ManyToOne
    @JoinColumn(name = "hpProfileId", referencedColumnName = "id")
    private HpProfileMaster hpProfileMaster;

    @OneToOne
    @JoinColumn(name = "broad_speciality_id", referencedColumnName = "id")
    private BroadSpeciality broadSpeciality;

    private String superSpecialityName;
    private String fileName;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = STATE_MEDICAL_COUNCIL_ID,referencedColumnName = ID)
    private StateMedicalCouncil stateMedicalCouncil;

    private java.util.Date registrationDate;
    private String registrationNumber;

}