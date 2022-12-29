package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.Address;
import lombok.Data;

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
