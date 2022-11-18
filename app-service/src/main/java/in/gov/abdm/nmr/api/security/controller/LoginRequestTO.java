package in.gov.abdm.nmr.api.security.controller;

import lombok.Data;

@Data
public class LoginRequestTO {

    private String username;
    private String password;
}
