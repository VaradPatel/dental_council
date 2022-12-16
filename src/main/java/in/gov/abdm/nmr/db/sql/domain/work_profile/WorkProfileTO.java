package in.gov.abdm.nmr.db.sql.domain.work_profile;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import in.gov.abdm.nmr.db.sql.domain.address.Address;
import in.gov.abdm.nmr.db.sql.domain.work_nature.WorkNatureTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
