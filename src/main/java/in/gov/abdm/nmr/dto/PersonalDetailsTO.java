package in.gov.abdm.nmr.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_BLANK_ERROR_MSG;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class PersonalDetailsTO {

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String salutation;

    private String aadhaarToken;

    private String firstName;
    private String middleName;
    private String lastName;
    private String fatherName;
    private String motherName;
    private String spouseName;
    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private NationalityTO countryNationality;
    private Date dateOfBirth;
    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String gender;
    private ScheduleTO schedule;
    private byte[] profilePhoto;
    private String fullName;

    private Boolean isNew;

}