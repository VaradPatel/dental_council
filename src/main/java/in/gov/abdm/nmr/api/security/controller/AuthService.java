package in.gov.abdm.nmr.api.security.controller;

import static in.gov.abdm.nmr.common.CustomHeaders.ACCESS_TOKEN;
import static in.gov.abdm.nmr.common.CustomHeaders.REFRESH_TOKEN;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.security.controller.to.LoginResponseTO;
import in.gov.abdm.nmr.api.security.jwt.JwtTypeEnum;
import in.gov.abdm.nmr.api.security.jwt.JwtUtil;
import in.gov.abdm.nmr.db.sql.domain.college.ICollegeDaoService;
import in.gov.abdm.nmr.db.sql.domain.college_dean.ICollegeDeanDaoService;
import in.gov.abdm.nmr.db.sql.domain.college_registrar.ICollegeRegistrarDaoService;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.HpProfile;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.IHpProfileDaoService;
import in.gov.abdm.nmr.db.sql.domain.nmc_profile.INmcDaoService;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.IStateMedicalCouncilDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.IUserDaoService;
import in.gov.abdm.nmr.db.sql.domain.user.User;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubTypeEnum;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserTypeEnum;

@Service
public class AuthService implements IAuthService {

    private JwtUtil jwtUtil;

    private IUserDaoService userDetailDaoService;

    private IHpProfileDaoService hpProfileService;

    private ICollegeDaoService collegeDaoService;

    private ICollegeDeanDaoService collegeDeanDaoService;

    private ICollegeRegistrarDaoService collegeRegistrarDaoService;

    private IStateMedicalCouncilDaoService stateMedicalCouncilService;

    private INmcDaoService nmcDaoService;

    public AuthService(JwtUtil jwtUtil, IUserDaoService userDetailDaoService, IHpProfileDaoService hpProfileService, ICollegeDaoService collegeDaoService, ICollegeDeanDaoService collegeDeanDaoService, ICollegeRegistrarDaoService collegeRegistrarDaoService, IStateMedicalCouncilDaoService stateMedicalCouncilService, INmcDaoService nmcDaoService) {
        this.jwtUtil = jwtUtil;
        this.userDetailDaoService = userDetailDaoService;
        this.hpProfileService = hpProfileService;
        this.collegeDaoService = collegeDaoService;
        this.collegeDeanDaoService = collegeDeanDaoService;
        this.collegeRegistrarDaoService = collegeRegistrarDaoService;
        this.stateMedicalCouncilService = stateMedicalCouncilService;
        this.nmcDaoService = nmcDaoService;
    }

    @Override
    public LoginResponseTO login(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        generateAccessAndRefreshToken(response, username);
        return createLoginResponse(username);
    }

    @Override
    public LoginResponseTO refreshToken(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        generateAccessAndRefreshToken(response, username);
        return createLoginResponse(username);
    }

    private LoginResponseTO createLoginResponse(String username) {
        User userDetail = userDetailDaoService.findUserDetailByUsername(username);

        LoginResponseTO loginResponseTO = new LoginResponseTO();
        loginResponseTO.setUserType(userDetail.getUserType().getId());
        loginResponseTO.setUserSubType(userDetail.getUserSubType().getId());

        if (UserTypeEnum.HEALTH_PROFESSIONAL.getCode().equals(userDetail.getUserType().getId())) {
            HpProfile hp = hpProfileService.findByUserDetail(userDetail.getId());
            loginResponseTO.setProfileId(hp.getId());
            loginResponseTO.setHpRegistered(StringUtils.isBlank(hp.getNmrId()));
            loginResponseTO.setBlacklisted(false);

        } else if (UserTypeEnum.COLLEGE.getCode().equals(userDetail.getUserType().getId())) {

            if (UserSubTypeEnum.COLLEGE.getCode().equals(userDetail.getUserSubType().getId())) {
                loginResponseTO.setProfileId(collegeDaoService.findByUserDetail(userDetail.getId()).getId());

            } else if (UserSubTypeEnum.COLLEGE_DEAN.getCode().equals(userDetail.getUserSubType().getId())) {
                loginResponseTO.setProfileId(collegeDeanDaoService.findByUserDetail(userDetail.getId()).getId());

            } else if (UserSubTypeEnum.COLLEGE_REGISTRAR.getCode().equals(userDetail.getUserSubType().getId())) {
                loginResponseTO.setProfileId(collegeRegistrarDaoService.findByUserDetail(userDetail.getId()).getId());

            }

        } else if (UserTypeEnum.STATE_MEDICAL_COUNCIL.getCode().equals(userDetail.getUserType().getId())) {
            loginResponseTO.setProfileId(stateMedicalCouncilService.findByUserDetail(userDetail.getId()).getId());

        } else if (UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getCode().equals(userDetail.getUserType().getId())) {
            loginResponseTO.setProfileId(nmcDaoService.findByUserDetail(userDetail.getId()).getId());
        }
        return loginResponseTO;
    }

    private void generateAccessAndRefreshToken(HttpServletResponse response, String username) {
        response.setHeader(ACCESS_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.ACCESS_TOKEN));
        response.setHeader(REFRESH_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.REFRESH_TOKEN));
    }
}
