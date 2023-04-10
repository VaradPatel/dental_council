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
    //    private Object placeOfBirth;
    @Field(name = "system_of_medicine")
    private String systemOfMedicine;

    @Field(name = "professional_type")
    private String professionalType;
    //  private boolean hasValidMobileNumber;
//  private boolean hasValidEmail;
    private String name;

    private List<Address> address;

    @Field(name = "registration_details")
    private List<RegistrationDetails> registrationDetails;


//
//
//
//    private String categoryName;
//


//    private Object councilRecordMetadata;

//    private String dateOfBirth;
//
//    private String email;
//
//    private String emptyMandatoryFields;


//    private String fullName;
//    private String gender;
//
//    private String id;
//    private String identifiedStateName;
//
//    private boolean isManually;
//


//    private List<String> languagesSpoken;
//

//


//    private Object metaData;

//    private String middleName;
//
//    private String mobileNumber;
//

//
//
//

//

//
//    private String otherCategory;
//
//    private String panNumber;


//    private String primaryContactNo;
//
//
//
//    private String profilePhoto;
//

//
//    private String salutation;

//    private List<String> specialities;


//


//    private Object workDetails;

//    private String workExperienceInYear;


//  private List<Specialities> specialities;

//  private Metadata metadata;

}
