package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QualificationDetailsMaster {

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
    
//    private BigInteger districtId;

    @OneToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id")
    private College college;

//    private BigInteger collegeId;

    @OneToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
    
//    private BigInteger countryId;

    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
//    private BigInteger courseId;

    @OneToOne
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private State state;
//    private BigInteger stateId;

    @OneToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private University university;
    
//    private BigInteger universityId;

    @ManyToOne
    @JoinColumn(name = "registration_details_id", referencedColumnName = "id")
    private RegistrationDetails registrationDetails;

    @ManyToOne
    @JoinColumn(name = "hpProfileId", referencedColumnName = "id")
    private HpProfileMaster hpProfileMaster;

    
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "qualificationStatus", referencedColumnName = "id")
//    private QualificationStatus qualificationStatus;

}
