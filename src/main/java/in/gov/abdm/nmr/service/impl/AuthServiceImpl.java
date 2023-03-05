package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.LoginResponseTO;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

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
        User userDetail = userDetailDaoService.findByUsername(username);

        generateAccessAndRefreshToken(response, username, userDetail);
        return createLoginResponse(userDetail);
    }

    private LoginResponseTO createLoginResponse(User userDetail) {
        LoginResponseTO loginResponseTO = new LoginResponseTO();
        loginResponseTO.setUserType(userDetail.getUserType().getId());
        loginResponseTO.setUserGroupId(userDetail.getGroup().getId());
        loginResponseTO.setUserId(userDetail.getId());
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getCode().equals(userDetail.getUserType().getId())) {
            HpProfile hp = hpProfileService.findLatestEntryByUserid(userDetail.getId());
            if (hp != null) {
                loginResponseTO.setProfileId(hp.getId());
                loginResponseTO.setHpRegistered(StringUtils.isBlank(hp.getNmrId()));
                loginResponseTO.setBlacklisted(HpProfileStatus.BLACKLISTED.getId() == hp.getHpProfileStatus().getId() ||
                        HpProfileStatus.SUSPENDED.getId() == hp.getHpProfileStatus().getId());
            }
        } else if (UserTypeEnum.COLLEGE.getCode().equals(userDetail.getUserType().getId())) {
            loginResponseTO.setUserSubType(userDetail.getUserSubType().getId());

            if (UserSubTypeEnum.COLLEGE.getCode().equals(userDetail.getUserSubType().getId())) {
                loginResponseTO.setProfileId(collegeDaoService.findByUserId(userDetail.getId()).getId());

            } else if (UserSubTypeEnum.COLLEGE_DEAN.getCode().equals(userDetail.getUserSubType().getId())) {
                loginResponseTO.setProfileId(collegeDeanDaoService.findByUserId(userDetail.getId()).getId());

            } else if (UserSubTypeEnum.COLLEGE_REGISTRAR.getCode().equals(userDetail.getUserSubType().getId())) {
                loginResponseTO.setProfileId(collegeRegistrarDaoService.findByUserId(userDetail.getId()).getId());

            }

        } else if (UserTypeEnum.STATE_MEDICAL_COUNCIL.getCode().equals(userDetail.getUserType().getId())) {
            loginResponseTO.setProfileId(smcProfileDaoService.findByUserId(userDetail.getId()).getId());

        } else if (UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getCode().equals(userDetail.getUserType().getId())) {
            loginResponseTO.setProfileId(nmcDaoService.findByUserId(userDetail.getId()).getId());
        } else if (UserTypeEnum.NBE.getCode().equals(userDetail.getUserType().getId())) {
            loginResponseTO.setProfileId(nbeDaoService.findByUserId(userDetail.getId()).getId());
        }
        return loginResponseTO;
    }

    private void generateAccessAndRefreshToken(HttpServletResponse response, String username, User userDetail) {
        String role = null;
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getCode().equals(userDetail.getUserType().getId())) {
            role = RoleConstants.ROLE_HEALTH_PROFESSIONAL;

        } else if (UserTypeEnum.COLLEGE.getCode().equals(userDetail.getUserType().getId())) {

            if (UserSubTypeEnum.COLLEGE.getCode().equals(userDetail.getUserSubType().getId())) {
                role = RoleConstants.ROLE_COLLEGE_ADMIN;
            } else if (UserSubTypeEnum.COLLEGE_DEAN.getCode().equals(userDetail.getUserSubType().getId())) {
                role = RoleConstants.ROLE_COLLEGE_DEAN;
            } else if (UserSubTypeEnum.COLLEGE_REGISTRAR.getCode().equals(userDetail.getUserSubType().getId())) {
                role = RoleConstants.ROLE_COLLEGE_REGISTRAR;
            }

        } else if (UserTypeEnum.STATE_MEDICAL_COUNCIL.getCode().equals(userDetail.getUserType().getId())) {
            role = RoleConstants.ROLE_STATE_MEDICAL_COUNCIL;

        } else if (UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getCode().equals(userDetail.getUserType().getId())) {
            role = RoleConstants.ROLE_NATIONAL_MEDICAL_COUNCIL;
        } else if (UserTypeEnum.NBE.getCode().equals(userDetail.getUserType().getId())) {
            role = RoleConstants.ROLE_NBE;
        }
        response.setHeader(ACCESS_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.ACCESS_TOKEN, role));
        response.setHeader(REFRESH_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.REFRESH_TOKEN, role));
    }
}
