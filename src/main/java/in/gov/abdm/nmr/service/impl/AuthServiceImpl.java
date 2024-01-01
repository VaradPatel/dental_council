package in.gov.abdm.nmr.service.impl;

import static in.gov.abdm.nmr.common.CustomHeaders.ACCESS_TOKEN;
import static in.gov.abdm.nmr.common.CustomHeaders.REFRESH_TOKEN;

import java.math.BigInteger;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.redis.hash.BlacklistToken;
import in.gov.abdm.nmr.redis.repository.IBlacklistTokenRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IWorkFlowRepository;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import in.gov.abdm.nmr.security.username_password.UserPasswordAuthenticationToken;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
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

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    IUserDaoService userDetailDaoService;
    @Autowired
    IHpProfileDaoService hpProfileService;
    @Autowired
    ICollegeProfileDaoService collegeProfileDaoService;
    @Autowired
    ISmcProfileDaoService smcProfileDaoService;
    @Autowired
    INmcDaoService nmcDaoService;
    @Autowired
    INbeDaoService nbeDaoService;
    @Autowired
    GatewayFClient gatewayFClient;
    @Autowired
    IWorkFlowRepository workFlowRepository;
    @Autowired
    IHpProfileRepository iHpProfileRepository;
    @Autowired
    IBlacklistTokenRepository blacklistTokenRepository;

    @Override
    public LoginResponseTO successfulAuth(HttpServletResponse response) {
        System.out.println("Entered in auth service");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((UserPasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType();

        User user = userDetailDaoService.findByUsername(username, userType);
        System.out.println("User is"+ user);
        LoginResponseTO loginResponse = createLoginResponse(user);
//        loginResponse.setEsignStatus(1);
        userDetailDaoService.updateLastLogin(user.getId());
        generateAccessAndRefreshToken(response, username, user, loginResponse.getProfileId());
        return loginResponse;
    }

    @Override
    public LoginResponseTO successfulAuthRefreshToken(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();

        User user = userDetailDaoService.findByUsername(username, userType);

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
        System.out.println("entered create login response");
        LoginResponseTO loginResponseTO = new LoginResponseTO();
        loginResponseTO.setUserType(user.getUserType().getId());
        loginResponseTO.setUserGroupId(user.getGroup().getId());
        loginResponseTO.setUserId(user.getId());
        loginResponseTO.setLastLogin(user.getLastLogin());
        loginResponseTO.setIsAdmin(false);
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getId().equals(user.getUserType().getId())) {
            System.out.println("in group_id 1");
            createHealthProfessionalLoginResponse(user, loginResponseTO);

        } else if (UserTypeEnum.COLLEGE.getId().equals(user.getUserType().getId())) {
            BigInteger userSubTypeId = user.getUserSubType().getId();
            loginResponseTO.setUserSubType(userSubTypeId);
            if(userSubTypeId != null && UserSubTypeEnum.COLLEGE_ADMIN.getId().equals(userSubTypeId)){
                loginResponseTO.setIsAdmin(true);
            }
            CollegeProfile collegeProfile = collegeProfileDaoService.findByUserId(user.getId());
                loginResponseTO.setProfileId(collegeProfile.getId());
                loginResponseTO.setCollegeId(collegeProfile.getCollege().getId());

        } else if (UserTypeEnum.SMC.getId().equals(user.getUserType().getId())) {
            BigInteger userSubTypeId = user.getUserSubType().getId();
            if(userSubTypeId != null && UserSubTypeEnum.SMC_ADMIN.getId().equals(userSubTypeId)){
                loginResponseTO.setIsAdmin(true);
            }
            loginResponseTO.setProfileId(smcProfileDaoService.findByUserId(user.getId()).getId());

        } else if (UserTypeEnum.NMC.getId().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(nmcDaoService.findByUserId(user.getId()).getId());
            BigInteger userSubTypeId = user.getUserSubType().getId();
            loginResponseTO.setUserSubType(userSubTypeId);
            if(userSubTypeId != null && UserSubTypeEnum.NMC_ADMIN.getId().equals(userSubTypeId)){
                loginResponseTO.setIsAdmin(true);
            }
        } else if (UserTypeEnum.NBE.getId().equals(user.getUserType().getId())) {
            loginResponseTO.setProfileId(nbeDaoService.findByUserId(user.getId()).getId());
            BigInteger userSubTypeId = user.getUserSubType().getId();
            if(userSubTypeId != null && UserSubTypeEnum.NBE_ADMIN.getId().equals(userSubTypeId)){
                loginResponseTO.setIsAdmin(true);
            }
        }
        return loginResponseTO;
    }

    private void createHealthProfessionalLoginResponse(User user, LoginResponseTO loginResponseTO) {
        HpProfile hp = hpProfileService.findLatestEntryByUserid(user.getId());
        if (hp != null) {
            loginResponseTO.setProfileId(hp.getId());
            loginResponseTO.setHpRegistered(StringUtils.isBlank(hp.getNmrId()));
            loginResponseTO.setEsignStatus(NMRUtil.getDerivedESignStatus(hp.getESignStatus(), hp.getModESignStatus()));
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
    }

    private void generateAccessAndRefreshToken(HttpServletResponse response, String username, User user, BigInteger profileId) {
        String[] roles = null;
        if (user.getUserSubType() == null) {
            roles = user.getUserType().getRoles().split(",");
        } else {
            roles = ArrayUtils.addAll(user.getUserType().getRoles().split(","), user.getUserSubType().getRoles().split(","));
        }
        if (user.getUserName() != null && UserTypeEnum.HEALTH_PROFESSIONAL.getId().equals(user.getUserType().getId())) {
            username = user.getUserName();
        }
        response.setHeader(ACCESS_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.ACCESS_TOKEN, roles, profileId, user.getUserType().getId()));
        if (!UserTypeEnum.SYSTEM.getId().equals(user.getUserType().getId())) {
            response.setHeader(REFRESH_TOKEN, jwtUtil.generateToken(username, JwtTypeEnum.REFRESH_TOKEN, roles, profileId, user.getUserType().getId()));
        }
    }

    @Override
    public ResponseMessageTo logOut(String token){

        BlacklistToken blacklistToken =new BlacklistToken();
        String[] tokenParts=token.split("\\.");
        blacklistToken.setToken(tokenParts[2]);
        blacklistToken.setExpired(true);
        blacklistToken.setTimeToLive(15);

        blacklistTokenRepository.save(blacklistToken);
        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
    }
}
