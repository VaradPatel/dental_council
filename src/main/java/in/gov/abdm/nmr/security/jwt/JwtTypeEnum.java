package in.gov.abdm.nmr.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtTypeEnum {

    ACCESS_TOKEN("at"), REFRESH_TOKEN("rt");

    private String code;
}
