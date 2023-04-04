package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.jpa.entity.Address;
import lombok.Data;

import java.math.BigInteger;

@Data
public class WorkProfileTO {
    private Integer id;

    private Address address;
    private Integer facility;
    private Integer isUserCurrentlyWorking;
    private String pincode;
    private String proofOfWorkAttachment;
    private String url;
    private BigInteger districtId;
    private BigInteger userId;
    private BigInteger broadSpecialityId;
    private BigInteger stateId;
    private BigInteger workNatureId;
    private BigInteger workstatusId;
    private BigInteger hpProfileId;
    private String workOrganization;
    private BigInteger organizationType;
}
