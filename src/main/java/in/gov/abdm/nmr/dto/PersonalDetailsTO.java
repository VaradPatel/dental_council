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

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Data
public class PersonalDetailsTO {

    @Salutation
    private String salutation;

    private String aadhaarToken;
    @OptionalName(message = INVALID_FIRST_NAME)
    private String firstName;
    @OptionalName(message = INVALID_MIDDLE_NAME)
    private String middleName;
    @OptionalName(message = INVALID_LAST_NAME)
    private String lastName;
    @OptionalName(message = INVALID_FATHER_NAME)
    private String fatherName;
    @OptionalName(message = INVALID_MOTHER_NAME)
    private String motherName;
    @OptionalName(message = INVALID_SPOUSE_NAME)
    private String spouseName;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private NationalityTO countryNationality;
    private Date dateOfBirth;
    @Gender
    private String gender;
    private String profilePhoto;
    @Name(message = INVALID_FULL_NAME)
    private String fullName;
    private Boolean isNew;
    @Email
    private String email;
    @MobileNumber
    private String mobile;

}