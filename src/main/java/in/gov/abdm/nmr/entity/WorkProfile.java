package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkProfile extends CommonAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;
    private String address;
    private String facilityId;
    private BigInteger facilityTypeId;
    private Integer isUserCurrentlyWorking;
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] proofOfWorkAttachment;
    private String url;
    private BigInteger userId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_nature_id", referencedColumnName = "id")
    private WorkNature workNature;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_status_id", referencedColumnName = "id")
    private WorkStatus workStatus;
    private BigInteger hpProfileId;
    private String workOrganization;
    @OneToOne
    @JoinColumn(name = "stateId", referencedColumnName = "id")
    private State state;
    @OneToOne
    @JoinColumn(name = "districtId", referencedColumnName = "id")
    private District district;
    private String organizationType;
    private String requestId;
    private String pincode;
    private String registrationNo;
    private Integer experienceInYears;
    private boolean deleteStatus;
    private String systemOfMedicine;
    private String department;
    private String designation;
    private String reason;
    private String remark;
}
