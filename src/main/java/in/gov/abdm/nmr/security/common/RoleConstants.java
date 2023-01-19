package in.gov.abdm.nmr.security.common;

public class RoleConstants {

    private RoleConstants() {
    }

    public static final String HEALTH_PROFESSIONAL = "HEALTH_PROFESSIONAL";
    public static final String STATE_MEDICAL_COUNCIL = "SMC";
    public static final String NATIONAL_MEDICAL_COUNCIL = "NMC";
    public static final String COLLEGE_DEAN = "COLLEGE_DEAN";
    public static final String COLLEGE_REGISTRAR ="COLLEGE_REGISTRAR";
    public static final String COLLEGE_ADMIN = "COLLEGE_ADMIN";
    public static final String NBE = "NBE";

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_HEALTH_PROFESSIONAL = ROLE_PREFIX + HEALTH_PROFESSIONAL;
    public static final String ROLE_STATE_MEDICAL_COUNCIL = ROLE_PREFIX + STATE_MEDICAL_COUNCIL;
    public static final String ROLE_NATIONAL_MEDICAL_COUNCIL = ROLE_PREFIX + NATIONAL_MEDICAL_COUNCIL;
    public static final String ROLE_COLLEGE_DEAN = ROLE_PREFIX + COLLEGE_DEAN;
    public static final String ROLE_COLLEGE_REGISTRAR = ROLE_PREFIX + COLLEGE_REGISTRAR;
    public static final String ROLE_COLLEGE_ADMIN = ROLE_PREFIX + COLLEGE_ADMIN;
    public static final String ROLE_NBE = ROLE_PREFIX + NBE;
}
