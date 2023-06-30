package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "collegeMaster")
@Table
public class CollegeMaster extends CommonAuditEntity {
    @Id
    @GenericGenerator(name = "incrementGenerator", strategy = "increment")
    @GeneratedValue(generator = "incrementGenerator")
    private BigInteger id;
    private String name;
    private BigInteger status;
    private BigInteger visibleStatus;
    private BigInteger systemOfMedicineId;

    @ManyToOne
    @JoinColumn(name = "stateId")
    private State state;

    @ManyToOne
    @JoinColumn(name = "stateMedicalCouncilId")
    private StateMedicalCouncil stateMedicalCouncil;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;
    private String collegeCode;
    private String website;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private District district;

    @ManyToOne
    @JoinColumn(name = "village_id", referencedColumnName = "id")
    private Villages village;

    private String pinCode;
}
