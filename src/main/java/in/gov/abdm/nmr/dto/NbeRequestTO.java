package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class NbeRequestTO {
    private BigInteger id;
    private String rollNo;
    private String result;
    private String year;
    private String month;
    private Integer marksObtained;
}
