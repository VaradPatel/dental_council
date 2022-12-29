package in.gov.abdm.nmr.dto;

import java.sql.Date;
import java.util.List;

import in.gov.abdm.nmr.dto.LanguageTO;
import in.gov.abdm.nmr.dto.NationalityTO;
import in.gov.abdm.nmr.dto.ScheduleTO;
import lombok.Data;

@Data
public class PersonalDetailsTO {
	private String salutation;
	private String aadhaarToken;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fatherName;
    private String motherName;
    private String spouseName;
    private NationalityTO countryNationality;
    private List<LanguageTO> language;
    private Date dateOfBirth;
    private String gender;
    private ScheduleTO schedule;
}
