package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.*;

import in.gov.abdm.nmr.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

//    @OneToMany(mappedBy = "subdistrict", fetch = FetchType.LAZY)
//    private List<City> cities;
    
    @OneToOne(mappedBy = "workProfile", fetch = FetchType.LAZY)
    private Address address;
    
    private Integer facility;
    private Integer isUserCurrentlyWorking;
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
    
//    @OneToOne(mappedBy = "workProfile", fetch = FetchType.LAZY)
    private BigInteger organizationType;
    private String requestId;
}
