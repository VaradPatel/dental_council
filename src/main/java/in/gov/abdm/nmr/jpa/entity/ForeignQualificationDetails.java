package in.gov.abdm.nmr.jpa.entity;

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
public class ForeignQualificationDetails {

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

    private BigInteger broadSpecialityId;
    private String superSpecialityName;
    private String fileName;
}
