package in.gov.abdm.nmr.entity;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QualificationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;
    private String certificate;
    private Date endDate;
    private Integer isNameChange;
    private Integer isVerified;
    private String name;
    private String nameChangeProofAttach;
    private String qualificationMonth;
    private String qualificationYear;
    private Date startDate;
    private String systemOfMedicine;

//    @OneToOne
//    @JoinColumn(name = "district")
//    private District district;
    
    private BigInteger districtId;
    
//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name = "college")
//    private College college;

    private BigInteger collegeId;
    
//    @OneToOne
//    @JoinColumn(name = "country")
//    private Country country;
    
    private BigInteger countryId;

//    @OneToOne
//    @JoinColumn(name = "course")
//    private FacilityType course;
    private BigInteger courseId;

//    @OneToOne
//    @JoinColumn(name = "state")
//    private State state;
    private BigInteger stateId;

//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name = "university")
//    private University university;
    
    private BigInteger universityId;

    @ManyToOne
    @JoinColumn(name = "registration_details_id", referencedColumnName = "id")
    private RegistrationDetails registrationDetails;

    @ManyToOne
    @JoinColumn(name = "hpProfileId", referencedColumnName = "id")
    private HpProfile hpProfile;

    
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "qualificationStatus", referencedColumnName = "id")
//    private QualificationStatus qualificationStatus;

}
