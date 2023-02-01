package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * this class represent request class of track application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchTrackApplicationRequestTO {

    /**
     * hpID is hp profile id.
     */
    private BigInteger hpId;

    /**
     * applicationType is used to filter api response.
     */
    private String applicationType;

    /**
     * page filed except number. to separating digital content into different pages on a website.
     */
    private int page;

    /**
     * size filed except number. The user can choose to view the required number of records per page.
     */
    private int size;

    /**
     * sortBy filed except name of filed. The sorting mechanism places the resources in order.
     */
    private String sortBy;

}