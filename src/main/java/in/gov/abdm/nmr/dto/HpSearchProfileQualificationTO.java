package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class HpSearchProfileQualificationTO {

    private String qualification;
    private String qualificationYear;
    private String universityName;
    @JsonIgnore
    private Timestamp createdAt;
}
