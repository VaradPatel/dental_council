package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.Email;
import in.gov.abdm.nmr.annotation.Name;
import in.gov.abdm.nmr.annotation.OptionalName;
import in.gov.abdm.nmr.annotation.Salutation;
import in.gov.abdm.validator.Gender;
import in.gov.abdm.validator.MobileNumber;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Date;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class PersonalDetailsTO {

    @Salutation
    private String salutation;

    private String aadhaarToken;
    @OptionalName(message = "Invalid first name")
    private String firstName;
    @OptionalName(message = "Invalid middle name")
    private String middleName;
    @OptionalName(message = "Invalid last name")
    private String lastName;
    @OptionalName(message = "Invalid father name")
    private String fatherName;
    @OptionalName(message = "Invalid mother name")
    private String motherName;
    @OptionalName(message = "Invalid spouse name")
    private String spouseName;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private NationalityTO countryNationality;
    private Date dateOfBirth;
    @Gender
    private String gender;
    private String profilePhoto;
    @Name(message = "Invalid full name")
    private String fullName;
    private Boolean isNew;
    @Email
    private String email;
    @MobileNumber
    private String mobile;

}