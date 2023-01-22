package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class NbeResponseTo {
    private BigInteger id;
    private String roll_no;
    private String result;
    private String year;
    private String month;
    private Integer marks_obtained;
}
