package in.gov.abdm.nmr.dto;

import java.util.List;

import lombok.Data;

@Data
public class HpSearchProfileTO {

    private String fullName;
    private String salutation;
    private String stateMedicalCouncil;
    private String fatherHusbandName;
    private String dateOfBirth;
    private String mobileNumber;
    private String email;
    List<HpSearchProfileQualificationTO> qualifications;
    private String yearOfInfo;
    private String registrationNumber;
    private String dateOfRegistration;
    private String uprnNumber;
    private String nmrId;
}
