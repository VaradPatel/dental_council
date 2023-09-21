package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.annotation.NotNullBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDetailTO {

    @NotNullBlank(message = SELECT_REGISTRATION_DATE)
    private Date registrationDate;
    @NotNullBlank(message = SELECT_REGISTRATION_NUMBER)
    private String registrationNumber;
    @NotNullBlank(message = NOT_NULL_ERROR_MSG)
    private StateMedicalCouncilTO stateMedicalCouncil;
    private String isRenewable;
    private Date renewableRegistrationDate;
    private String isNameChange;
    private String registrationCertificate;
    private String fileName;
    private String fileType;
    private String nameChangeProofAttach;
    private String nameChangeProofAttachFileName;
    private String nameChangeProofAttachFileNameType;
}
