package in.gov.abdm.nmr.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
    private String proofOfWorkAttachment;
    private String url;
    private BigInteger userId;
    private BigInteger broadSpecialityId;
    private BigInteger workNatureId;
    private BigInteger workStatusId;
    private BigInteger hpProfileId;
    private String workOrganization;
    
//    @OneToOne(mappedBy = "workProfile", fetch = FetchType.LAZY)
    private BigInteger organizationType;
}
