package in.gov.abdm.nmr.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WorkProfileMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

//    @OneToMany(mappedBy = "subdistrict", fetch = FetchType.LAZY)
//    private List<City> cities;

    private String address;

    private BigInteger facilityId;
    private BigInteger facilityTypeId;
    private Integer isUserCurrentlyWorking;
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] proofOfWorkAttachment;
    private String url;
    private BigInteger userId;
    private BigInteger broadSpecialityId;
    private BigInteger workNatureId;
    private BigInteger workStatusId;
    private BigInteger hpProfileId;
    private String workOrganization;

    //    @OneToOne(mappedBy = "workProfile", fetch = FetchType.LAZY)
    private String organizationType;

    private String requestId;

}
