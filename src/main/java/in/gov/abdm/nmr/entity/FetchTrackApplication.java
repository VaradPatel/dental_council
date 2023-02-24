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
    private String request_id;
    private BigInteger application_type_id;
    private Date created_at;
    private BigInteger work_flow_status_id;
    private BigInteger current_group_id;
    private Integer pendency_days;
}