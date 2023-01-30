package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchTrackApplicationResponseTO {

    private String request_id;
    private BigInteger application_type_id;
    private Date created_at;
    private BigInteger work_flow_status_id;
    private BigInteger current_group_id;
    private Integer pendency_days;
}
