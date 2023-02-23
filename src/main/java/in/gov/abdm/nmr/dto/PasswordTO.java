package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class PasswordTO {

    private int id;
    private String value;
    private UserTO userDetail;
}
