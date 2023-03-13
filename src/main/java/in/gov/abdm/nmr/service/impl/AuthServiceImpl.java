package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.dto.LoginResponseTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.dto.SessionResponseTo;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.security.common.RoleConstants;
import in.gov.abdm.nmr.security.jwt.JwtTypeEnum;
import in.gov.abdm.nmr.security.jwt.JwtUtil;
import in.gov.abdm.nmr.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

import static in.gov.abdm.nmr.common.CustomHeaders.ACCESS_TOKEN;
import static in.gov.abdm.nmr.common.CustomHeaders.REFRESH_TOKEN;

@Service
public class AuthServiceImpl implements IAuthService {

    private JwtUtil jwtUtil;

    private IUserDaoService userDetailDaoService;

    private IHpProfileDaoService hpProfileService;

    private ICollegeDaoService collegeDaoService;

    private ICollegeDeanDaoService collegeDeanDaoService;

    private ICollegeRegistrarDaoService collegeRegistrarDaoService;

    private ISmcProfileDaoService smcProfileDaoService;

    private INmcDaoService nmcDaoService;
    private INbeDaoService nbeDaoService;

    @Autowired
    GatewayFClient gatewayFClient;

    public AuthServiceImpl(JwtUtil jwtUtil, IUserDaoService userDetailDaoService, IHpProfileDaoService hpProfileService, ICollegeDaoService collegeDaoService, ICollegeDeanDaoService collegeDeanDaoService, ICollegeRegistrarDaoService collegeRegistrarDaoService, ISmcProfileDaoService smcProfileDaoService, INmcDaoService nmcDaoService, INbeDaoService nbeDaoService) {
        this.jwtUtil = jwtUtil;
        this.userDetailDaoService = userDetailDaoService;
        this.hpProfileService = hpProfileService;
        this.collegeDaoService = collegeDaoService;
        this.collegeDeanDaoService = collegeDeanDaoService;
        this.collegeRegistrarDaoService = collegeRegistrarDaoService;
        this.smcProfileDaoService = smcProfileDaoService;
        this.nmcDaoService = nmcDaoService;
        this.nbeDaoService = nbeDaoService;
    }

    @Override
    public LoginResponseTO successfulAuth(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailDaoService.findByUsername(username);

        LoginResponseTO loginResponse = createLoginResponse(user);
        generateAccessAndRefreshToken(response, username, user, loginResponse.getProfileId());
        return loginResponse;
    }

    @Override
    public SessionResponseTo sessions(SessionRequestTo sessionRequestTo) {
        return gatewayFClient.sessions(sessionRequestTo);
    }

    private LoginResponseTO createLoginResponse(User user) {
        LoginResponseTO loginResponseTO = new LoginResponseTO();
        loginResponseTO.setUserType(user.getUserType().getId());
        loginResponseTO.setUserGroupId(user.getGroup().getId());
        loginResponseTO.setUserId(user.getId());
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getCode().equals(user.getUserType().getId())) {
            HpProfile hp = hpProfileService.findLatestEntryByUserid(user.getId());
            if (hp != null) {
                loginResponseTO.setProfileId(hp.getId());
                loginResponseTO.setHpRegistered(StringUtils.isBlank(hp.getNmrId()));
                loginResponseTO.setBlacklisted(HpProfileStatus.BLACKLISTED.getId() == hp.getHpProfileStatus().getId() || HpProfileStatus.SUSPENDED.getId() == hp.getHpProfileStatus().getId());
            }
        } else if (UserTypeEnum.COLLEGE.getCode().equals(user.getUserType().getId())) {
            loginResponseTO.setUserSubType(user.getUserSubType().getId());

            if (UserSubTypeEnum.COLLEGE.getCode().equals(user.getUserSubType().getId())) {
                loginResponseTO.setProfileId(collegeDaoService.findByUserId(user.getId()).getId());

            } else if (UserSubTypeEnum.COLLEGE_DEAN.getCode().equals(user.getUserSubType().getId())) {
                CollegeDean collegeDean = collegeDeanDaoService.findByUserId(user.getId());
                loginResponseTO.setProfileId(collegeDean.getId());
                loginResponseTO.setParentProfileId(collegeDean.getCollege().getId());

            } else if (UserSubTypeEnum.COLLEGE_REGISTRAR.getCode().equals(user.getUserSubType().getId())) {
                CollegeRegistrar collegeRegistrar = collegeRegistrarDaoService.findByUserId(user.getId());
                loginResponseTO.setProfileId(collegeRegistrar.getId());
                loginResponseTO.setParentProfileId(collegeRegistrar.getCollege().getId());

            }

        } else if (UserTypeEnum.STATE_MEDICAL_COUNCIL.getCode().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(smcProfileDaoService.findByUserId(user.getId()).getId());

        } else if (UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getCode().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(nmcDaoService.findByUserId(user.getId()).getId());
        } else if (UserTypeEnum.NBE.getCode().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(nbeDaoService.findByUserId(user.getId()).getId());
        }
        return loginResponseTO;
    }

    private void generateAccessAndRefreshToken(HttpServletResponse response, String username, User user, BigInteger profileId) {
        String role = null;
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getCode().equals(user.getUserType().getId())) {
            role = RoleConstants.ROLE_HEALTH_PROFESSIONAL;

        } else if (UserTypeEnum.COLLEGE.getCode().equals(user.getUserType().getId())) {

            if (UserSubTypeEnum.COLLEGE.getCode().equals(user.getUserSubType().getId())) {
                role = RoleConstants.ROLE_COLLEGE_ADMIN;
            } else if (UserSubTypeEnum.COLLEGE_DEAN.getCode().equals(user.getUserSubType().getId())) {
                role = RoleConstants.ROLE_COLLEGE_DEAN;
            } else if (UserSubTypeEnum.COLLEGE_REGISTRAR.getCode().equals(user.getUserSubType().getId())) {
                role = RoleConstants.ROLE_COLLEGE_REGISTRAR;
            }

        } else if (UserTypeEnum.STATE_MEDICAL_COUNCIL.getCode().equals(user.getUserType().getId())) {
            role = RoleConstants.ROLE_STATE_MEDICAL_COUNCIL;

        } else if (UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getCode().equals(user.getUserType().getId())) {
            role = RoleConstants.ROLE_NATIONAL_MEDICAL_COUNCIL;
        } else if (UserTypeEnum.NBE.getCode().equals(user.getUserType().getId())) {
            role = RoleConstants.ROLE_NBE;
        } else if (UserTypeEnum.SYSTEM.getCode().equals(user.getUserType().getId())) {
            role = RoleConstants.ROLE_SYSTEM;
        }

        response.setHeader(ACCESS_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.ACCESS_TOKEN, role, profileId));
        if (!UserTypeEnum.SYSTEM.getCode().equals(user.getUserType().getId())) {
            response.setHeader(REFRESH_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.REFRESH_TOKEN, role, profileId));
        }
    }
}
