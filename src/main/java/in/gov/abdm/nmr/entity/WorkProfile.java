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
public class WorkProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;
    private String address;
    private BigInteger facilityId;
    private BigInteger facilityTypeId;
    private Integer isUserCurrentlyWorking;
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] proofOfWorkAttachment;
    private String url;
    private BigInteger userId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "broad_speciality_id", referencedColumnName = "id")
    private BroadSpeciality broadSpeciality;
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
}
