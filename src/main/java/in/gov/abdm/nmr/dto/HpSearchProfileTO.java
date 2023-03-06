package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.util.List;

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
    private String nmrId;
    private byte[] profilePhoto;
}
