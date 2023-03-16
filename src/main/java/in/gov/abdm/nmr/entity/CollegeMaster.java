package in.gov.abdm.nmr.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "college_master")
public class CollegeMaster extends CommonAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    private BigInteger status;
    private BigInteger visibleStatus;
    private BigInteger systemOfMedicineId;
    private BigInteger stateId;
    private BigInteger courseId;

}
