package in.gov.abdm.nmr.db.sql.domain.qualification_detail;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import in.gov.abdm.nmr.db.sql.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.db.sql.domain.registration_detail.RegistrationDetails;
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
    
    private BigInteger district;
    
//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name = "college")
//    private College college;

    private BigInteger college;
    
//    @OneToOne
//    @JoinColumn(name = "country")
//    private Country country;
    
    private BigInteger country;

//    @OneToOne
//    @JoinColumn(name = "course")
//    private Course course;
    private BigInteger course;

//    @OneToOne
//    @JoinColumn(name = "state")
//    private State state;
    private BigInteger state;

//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name = "university")
//    private University university;
    
    private BigInteger university;

    @ManyToOne
    @JoinColumn(name = "registrationDetail")
    private RegistrationDetails registrationDetails;

    @ManyToOne
    @JoinColumn(name = "hpProfileId", referencedColumnName = "id")
    private HpProfile hpProfile;

    
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "qualificationStatus", referencedColumnName = "id")
//    private QualificationStatus qualificationStatus;

}
