package in.gov.abdm.nmr.nosql.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualificationsDetails {
    @Field(name = "qualification_year")
    private String qualificationYear;
    @Field(name = "qualification_month")
    private String qualificationMonth;

    private String college;

    private String university;

    private String course;
    @Field(name = "system_of_medicine")
    private String systemOfMedicine;

    private String country;

    private String state;

    private String name;
}
