package in.gov.abdm.nmr.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class FetchTrackApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String requestId;
    private BigInteger applicationTypeId;
    private Date createdAt;
    private BigInteger workFlowStatusId;
    private BigInteger currentGroupId;
    private Integer pendencyDays;
}