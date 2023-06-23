package in.gov.abdm.nmr.service.impl;

import static in.gov.abdm.nmr.common.CustomHeaders.ACCESS_TOKEN;
import static in.gov.abdm.nmr.common.CustomHeaders.REFRESH_TOKEN;

import java.math.BigInteger;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IWorkFlowRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.dto.LoginResponseTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.dto.SessionResponseTo;
import in.gov.abdm.nmr.entity.CollegeProfile;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.ESignStatus;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.security.jwt.JwtTypeEnum;
import in.gov.abdm.nmr.security.jwt.JwtUtil;
import in.gov.abdm.nmr.service.IAuthService;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.service.INbeDaoService;
import in.gov.abdm.nmr.service.INmcDaoService;
import in.gov.abdm.nmr.service.ISmcProfileDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;

@Service
public class AuthServiceImpl implements IAuthService {

    private JwtUtil jwtUtil;

    private IUserDaoService userDetailDaoService;
    private IHpProfileDaoService hpProfileService;
    private ICollegeProfileDaoService collegeProfileDaoService;
    private ISmcProfileDaoService smcProfileDaoService;

    private INmcDaoService nmcDaoService;
    private INbeDaoService nbeDaoService;

    @Autowired
    GatewayFClient gatewayFClient;
    @Autowired
    private IWorkFlowRepository workFlowRepository;
    @Autowired
    private IHpProfileRepository iHpProfileRepository;

    public AuthServiceImpl(JwtUtil jwtUtil, IUserDaoService userDetailDaoService, IHpProfileDaoService hpProfileService, ICollegeProfileDaoService collegeProfileDaoService, ISmcProfileDaoService smcProfileDaoService, INmcDaoService nmcDaoService, INbeDaoService nbeDaoService) {
        this.jwtUtil = jwtUtil;
        this.userDetailDaoService = userDetailDaoService;
        this.hpProfileService = hpProfileService;
        this.collegeProfileDaoService = collegeProfileDaoService;
        this.smcProfileDaoService = smcProfileDaoService;
        this.nmcDaoService = nmcDaoService;
        this.nbeDaoService = nbeDaoService;
    }

    @Override
    public LoginResponseTO successfulAuth(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailDaoService.findByUsername(username);

        LoginResponseTO loginResponse = createLoginResponse(user);
        userDetailDaoService.updateLastLogin(user.getId());
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
        loginResponseTO.setLastLogin(user.getLastLogin());
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getId().equals(user.getUserType().getId())) {
            HpProfile hp = hpProfileService.findLatestEntryByUserid(user.getId());
            if (hp != null) {
                loginResponseTO.setProfileId(hp.getId());
                loginResponseTO.setHpRegistered(StringUtils.isBlank(hp.getNmrId()));
                loginResponseTO.setEsignStatus(hp.getESignStatus() != null ? hp.getESignStatus() : ESignStatus.PROFILE_NOT_ESIGNED.getId());
                HpProfile latestHpProfile = iHpProfileRepository.findLatestHpProfileFromWorkFlow(hp.getRegistrationId());
                if (latestHpProfile != null) {
                    loginResponseTO.setBlacklisted(Objects.equals(HpProfileStatus.BLACKLISTED.getId(), latestHpProfile.getHpProfileStatus().getId()) || Objects.equals(HpProfileStatus.SUSPENDED.getId(), latestHpProfile.getHpProfileStatus().getId()));
                    loginResponseTO.setHpProfileStatusId(latestHpProfile.getHpProfileStatus() != null ? latestHpProfile.getHpProfileStatus().getId() : null);
                    WorkFlow workFlow = workFlowRepository.findLastWorkFlowForHealthProfessional(latestHpProfile.getId());
                    if (workFlow != null) {
                        loginResponseTO.setWorkFlowStatusId(workFlow.getWorkFlowStatus().getId());
                    }
                } else {
                    loginResponseTO.setBlacklisted(Objects.equals(HpProfileStatus.BLACKLISTED.getId(), hp.getHpProfileStatus().getId()) || Objects.equals(HpProfileStatus.SUSPENDED.getId(), hp.getHpProfileStatus().getId()));
                    loginResponseTO.setHpProfileStatusId(hp.getHpProfileStatus() != null ? hp.getHpProfileStatus().getId() : null);
                    WorkFlow workFlow = workFlowRepository.findLastWorkFlowForHealthProfessional(hp.getId());
                    if (workFlow != null) {
                        loginResponseTO.setWorkFlowStatusId(workFlow.getWorkFlowStatus().getId());
                    }
                }
            }
        } else if (UserTypeEnum.COLLEGE.getId().equals(user.getUserType().getId())) {
            BigInteger userSubTypeId = user.getUserSubType().getId();
            loginResponseTO.setUserSubType(userSubTypeId);

            CollegeProfile collegeProfile = collegeProfileDaoService.findByUserId(user.getId());
                loginResponseTO.setProfileId(collegeProfile.getId());
                loginResponseTO.setCollegeId(collegeProfile.getCollege().getId());

        } else if (UserTypeEnum.SMC.getId().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(smcProfileDaoService.findByUserId(user.getId()).getId());

        } else if (UserTypeEnum.NMC.getId().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(nmcDaoService.findByUserId(user.getId()).getId());
            BigInteger userSubTypeId = user.getUserSubType().getId();
            loginResponseTO.setUserSubType(userSubTypeId);
        } else if (UserTypeEnum.NBE.getId().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(nbeDaoService.findByUserId(user.getId()).getId());
        }
        return loginResponseTO;
    }

    private void generateAccessAndRefreshToken(HttpServletResponse response, String username, User user, BigInteger profileId) {
        String[] roles = null;
        if (user.getUserSubType() == null) {
            roles = user.getUserType().getRoles().split(",");
        } else {
            roles = ArrayUtils.addAll(user.getUserType().getRoles().split(","), user.getUserSubType().getRoles().split(","));
        }

        response.setHeader(ACCESS_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.ACCESS_TOKEN, roles, profileId));
        if (!UserTypeEnum.SYSTEM.getId().equals(user.getUserType().getId())) {
            response.setHeader(REFRESH_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.REFRESH_TOKEN, roles, profileId));
        }
    }
}
