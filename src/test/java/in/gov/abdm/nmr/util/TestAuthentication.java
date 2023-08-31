package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import in.gov.abdm.nmr.security.jwt.JwtTypeEnum;

public class TestAuthentication extends JwtAuthenticationToken {

    public TestAuthentication(){
        this("",JwtTypeEnum.ACCESS_TOKEN,UserTypeEnum.HEALTH_PROFESSIONAL);
    }
    public TestAuthentication(String token, JwtTypeEnum type, UserTypeEnum userType) {
        super(token, type, userType);
    }
}