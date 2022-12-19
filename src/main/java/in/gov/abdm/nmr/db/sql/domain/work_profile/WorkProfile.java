package in.gov.abdm.nmr.db.sql.domain.work_profile;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import in.gov.abdm.nmr.db.sql.domain.address.Address;
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
    
    @OneToMany(mappedBy = "workProfile", fetch = FetchType.LAZY)
    private List<Address> address;
    
    private Integer facility;
    private Integer isUserCurrentlyWorking;
//    private String pincode;
    private String proofOfWorkAttachment;
    private String url;
//    private BigInteger districtId;
    private BigInteger userId;
    private BigInteger broadSpecialityId;
//    private BigInteger stateId;
    private BigInteger workNatureId;
    private BigInteger workStatusId;
    private BigInteger hpProfileId;
    private String workOrganization;
    
//    @OneToOne(mappedBy = "workProfile", fetch = FetchType.LAZY)
    private BigInteger organizationType;
}
