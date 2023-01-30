package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchTrackApplicationRequestTO {

    private BigInteger hpId;
    private String applicationType;

    private int page;
    private int size;
    private String sortBy;

}
