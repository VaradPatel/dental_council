package in.gov.abdm.nmr.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationProfileEnum {

    LOCAL("local"), BETA("beta"), SANDBOX("sandbox"), PRODUCTION("prod");
    private String code;
}
