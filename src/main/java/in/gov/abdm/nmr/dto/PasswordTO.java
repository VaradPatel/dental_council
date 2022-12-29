package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.dto.UserTO;
import lombok.Data;

@Data
public class PasswordTO {

    private int id;
    private String value;
    private UserTO userDetail;
}
