package in.gov.abdm.nmr.nosql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "council")
public class Council {
    private String id;

    @Field(name = "full_name")
    private String fullName;

    private String email;

    @Field(name = "pan_number")
    private String panNumber;

    @Field(name = "primary_contact_no")
    private String primaryContactNo;

    @Field(name = "mobile_number")
    private String mobileNumber;

    private String salutation;

    @Field(name = "profile_photo")
    private String profilePhoto;

    @Field(name = "date_of_birth")
    private String dateOfBirth;

    private String gender;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "middle_name")
    private String middleName;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "father_name")
    private String fatherName;

    @Field(name = "mother_name")
    private String motherName;

    @Field(name = "marital_status")
    private String maritalStatus;

    @Field(name = "spouse_name")
    private String spouseName;

    @Field(name = "changed_name")
    private String changedName;

    @Field(name = "official_telephone")
    private String officialTelephone;

    @Field(name = "aadhar_number")
    private String aadharNumber;

    @Field(name = "landline_code")
    private String landlineCode;

    @Field(name = "landline_public")
    private String landlinePublic;


    private String nationality;

    @Field(name = "system_of_medicine")
    private String systemOfMedicine;

    @Field(name = "professional_type")
    private String professionalType;

    private String name;

    private List<Address> address;

    @Field(name = "registration_details")
    private List<RegistrationsDetails> registrationsDetails;

}
