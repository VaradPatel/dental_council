package in.gov.abdm.nmr.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationDetails {
    @Field(name = "system_of_medicine")
    private String systemOfMedicine;
    @Field(name = "council_name")
    private String councilName;
    @Field(name = "registration_no")
    private String registrationNo;
    @Field(name = "registration_date")
    private String registrationDate;
    @Field(name = "medical_registration_date")
    private String medicalRegistrationDate;
    @Field(name = "qualification_details")
    private List<QualificationDetails> qualificationDetails;
}
