package in.gov.abdm.nmr.dto;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.NationalityTO;
import in.gov.abdm.nmr.dto.ScheduleTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static in.gov.abdm.nmr.util.NMRConstants.NOT_BLANK_ERROR_MSG;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_NULL_ERROR_MSG;

@Data
public class PersonalDetailsTO {

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    //@Pattern(regexp = "^(Mr|Ms|Mrs|Dr|Miss)\\\\.?$", message = "Mr., Ms., Mrs.,Dr., and Miss.")
    private String salutation;

    private String aadhaarToken;

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String firstName;
    private String middleName;
    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String lastName;
    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String fatherName;
    private String motherName;
    private String spouseName;

    @Valid
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private NationalityTO countryNationality;
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private List<LanguageTO> language;
    private Date dateOfBirth;
    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String gender;
    @NotNull(message = NOT_NULL_ERROR_MSG)
    private ScheduleTO schedule;
    private byte[] profilePhoto;
    private String fullName;

}