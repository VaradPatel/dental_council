package in.gov.abdm.nmr.db.sql.domain.user_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    HEALTH_PROFESSIONAL(1L, "Health Professional"), COLLEGE(2L, "College"), STATE_MEDICAL_COUNCIL(3L, "State Medical Council"), //
    NATIONAL_MEDICAL_COUNCIL(4L, "National Medical Council");

    private Long code;

    private String name;
}
