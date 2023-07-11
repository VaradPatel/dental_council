package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.Name;
import in.gov.abdm.nmr.annotation.OptionalName;
import in.gov.abdm.nmr.annotation.Salutation;
import in.gov.abdm.validator.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Date;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class PersonalDetailsTO {

    @Salutation
    private String salutation;

    private String aadhaarToken;
    @OptionalName
    private String firstName;
    @OptionalName
    private String middleName;
    @OptionalName
    private String lastName;
    @OptionalName
    private String fatherName;
    @OptionalName
    private String motherName;
    @OptionalName
    private String spouseName;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private NationalityTO countryNationality;
    private Date dateOfBirth;
    @Gender
    private String gender;
    private String profilePhoto;
    @Name
    private String fullName;
    private Boolean isNew;
    @Email
    private String email;
    @MobileNumber
    private String mobile;

}