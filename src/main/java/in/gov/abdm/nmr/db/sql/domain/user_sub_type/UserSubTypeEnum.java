package in.gov.abdm.nmr.db.sql.domain.user_sub_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserSubTypeEnum {

    COLLEGE(1L, "College", "College"), COLLEGE_REGISTRAR(2L, "College Registrar", "College"), COLLEGE_DEAN(3L, "College Dean", "College");

    private Long code;
    private String name;
    private String userType;
}
