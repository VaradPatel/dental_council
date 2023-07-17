package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class QueryResponseTo {

    private BigInteger id;
    private BigInteger hpProfileId;
    private String fieldName;
    private String fieldLabel;
    private String sectionName;
    private String queryComment;
    private String commonComment;
    private String queryBy;
    private String queryStatus;
    private String requestId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}