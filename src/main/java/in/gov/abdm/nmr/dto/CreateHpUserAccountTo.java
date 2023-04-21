package in.gov.abdm.nmr.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHpUserAccountTo {

    private String email;

    private String mobile;

    private String username;

    private String registrationNumber;

    private String password;

    private String hprId;

    private String hprIdNumber;

    private boolean isNew;

    private BigInteger stateMedicalCouncilId;
}