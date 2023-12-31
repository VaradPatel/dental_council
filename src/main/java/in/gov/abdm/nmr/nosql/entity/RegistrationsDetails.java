package in.gov.abdm.nmr.nosql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationsDetails {
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
    @Field(name = "is_renewable_registration")
    private boolean  isRenewableRegistration;
    @Field(name = "renewable_registration_date")
    private String renewableRegistrationDate;
    @Field(name = "type")
    private String type;
    @Field(name = "qualification_details")
    private List<QualificationsDetails> qualificationsDetails;
}
