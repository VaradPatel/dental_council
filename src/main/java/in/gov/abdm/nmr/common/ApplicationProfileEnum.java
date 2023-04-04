package in.gov.abdm.nmr.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationProfileEnum {

    LOCAL("local"), DEV("dev"), QA("qa"), SANDBOX("sandbox"), STAGING("staging"), PRODUCTION("prod");

    private final String code;
}
