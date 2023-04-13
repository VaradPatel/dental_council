package in.gov.abdm.nmr.nosql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Specialities {
    private String speciality;
    private String subSpeciality;
}