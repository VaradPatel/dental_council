package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.nosql.entity.Address;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.entity.QualificationsDetails;
import in.gov.abdm.nmr.nosql.entity.RegistrationsDetails;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import in.gov.abdm.nmr.service.*;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import in.gov.abdm.nmr.util.XSSFileDetection;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static in.gov.abdm.nmr.util.NMRConstants.*;
import static in.gov.abdm.nmr.util.NMRUtil.isFileTypeSupported;
import static in.gov.abdm.nmr.util.NMRUtil.validateQualificationDetailsAndProofs;

@Service
@Slf4j
public class HpRegistrationServiceImpl implements IHpRegistrationService {
    @Autowired
    private ICollegeMasterRepository iCollegeMasterRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private IHpProfileMapper iHpProfileMapper;

    @Autowired
    private CountryDtoMapper countryDtoMapper;

    @Autowired
    private IHpProfileMasterMapper iHpProfileAuditMapper;

    @Autowired
    private IWorkFlowService iWorkFlowService;

    @Autowired
    private IRequestCounterService requestCounterService;

    @Autowired
    private IWorkFlowRepository workFlowRepository;

    @Autowired
    private IWorkFlowAuditRepository iWorkFlowAuditRepository;

    @Autowired
    private IHpProfileMasterRepository iHpProfileMasterRepository;

    @Autowired
    private IHpProfileRepository iHpProfileRepository;

    @Autowired
    private IRegistrationDetailRepository registrationDetailRepository;

    @Autowired
    private RegistrationDetailMasterRepository registrationDetailAuditRepository;

    @Autowired
    private WorkProfileRepository workProfileRepository;

    @Autowired
    private IHpProfileDaoService hpProfileDaoService;

    @Autowired
    private IAddressRepository iAddressRepository;

    @Autowired
    private VillagesRepository villagesRepository;

    @Autowired
    private LanguagesKnownRepository languagesKnownRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private HpNbeDetailsRepository hpNbeDetailsRepository;

    @Autowired
    private IQualificationDetailRepository qualificationDetailRepository;

    @Autowired
    private IForeignQualificationDetailRepository customQualificationDetailRepository;

    @Autowired
    private BroadSpecialityRepository broadSpecialityRepository;

    @Autowired
    private IStateRepository stateRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private SubDistrictRepository subDistrictRepository;

    @Autowired
    private IRegistrationDetailRepository iRegistrationDetailRepository;

    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private IStateMedicalCouncilRepository iStateMedicalCouncilRepository;

    @Autowired
    private IHpProfileStatusRepository hpProfileStatusRepository;

    @Autowired
    IAddressMasterRepository iAddressMasterRepository;

    @Autowired
    private HpProfileRegistrationMapper hpProfileRegistrationMapper;

    @Autowired
    private IQueriesService iQueriesService;

    @Autowired
    IOtpValidationService otpValidationService;

    @Autowired
    private ICouncilService councilService;

    @Autowired
    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    @Autowired
    private UniversityMasterRepository universityMasterRepository;

    @Autowired
    private IQualificationDetailRepository iQualificationDetailRepository;

    @Autowired
    INotificationService notificationService;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Value("${council.email-verify.url}")
    private String emailVerifyUrl;

    @Autowired
    private IUserDaoService userDaoService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    RsaUtil rsaUtil;

    @Autowired
    private IPasswordDaoService passwordDaoService;
    @Autowired
    private IUserDaoService userDetailDaoService;

    @Autowired
    private QueriesRepository queriesRepository;

    @Autowired
    private ITrackApplicationReadStatusRepository iTrackApplicationReadStatusRepository;
    @Autowired
    private IAccessControlService accessControlService;
    @Autowired
    private ICollegeProfileRepository collegeProfileRepository;
    @Autowired
    IForeignQualificationDetailRepository iCustomQualificationDetailRepository;

    /**
     * This method fetches the SMC registration details for a given request.
     *
     * @param councilId
     * @param registrationNumber
     * @return SmcRegistrationDetailResponseTO A TO (Transfer Object) containing the SMC registration information that was fetched.
     */
    @Override
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber) throws NoDataFoundException {
        return iHpProfileMapper
                .smcRegistrationToDto(hpProfileDaoService.fetchSmcRegistrationDetail(councilId, registrationNumber));
    }


    @Override
    public HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException, InvalidRequestException, InvalidFileUploadException {
        validateUserAccessToResourceForHP(hpProfileId);
        return iHpProfileMapper.hpProfilePictureUploadToDto(hpProfileDaoService.uploadHpProfilePhoto(file, hpProfileId));
    }

    private void validateUserAccessToResourceForHP(BigInteger hpProfileId) {
        User loggedInUser = accessControlService.getLoggedInUser();
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getId().equals(loggedInUser.getUserType().getId())) {
            HpProfile hpProfile = iHpProfileRepository.findLatestEntryByUserid(loggedInUser.getId());
            if (!hpProfile.getId().equals(hpProfileId)) {
                log.error("Access denied: You do not have permissions to access this resource.");
                throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
            }
        } else {
            log.error("Access denied: You do not have permissions to access this resource.");
            throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
        }
    }

    /**
     * Adds a list of qualification details to the specified health professional's profile.
     *
     * @param hpProfileId                   The ID of the health professional's profile.
     * @param qualificationDetailRequestTOs A list of qualification detail requests as a string.
     * @param nameChangeCertificate
     * @return The string "Success" if the operation is successful.
     * @throws WorkFlowException          If an error occurs while initiating the submission workflow.
     * @throws InvalidFileUploadException
     * @throws IOException
     */
    @Override
    @Transactional
    public String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs, List<MultipartFile> proofs, MultipartFile proofOfQualificationNameChange) throws InvalidRequestException, WorkFlowException, IOException, InvalidFileUploadException {
        validateUserAccessToResourceForHP(hpProfileId);
        for (MultipartFile file : proofs) {
            isFileTypeSupported(file);
            if(XSSFileDetection.isMaliciousCodeInFile(file)) {
    			throw new InvalidFileUploadException();
            }
        }
        if (proofOfQualificationNameChange != null && StringUtils.isNotBlank(proofOfQualificationNameChange.getOriginalFilename())) {
        isFileTypeSupported(proofOfQualificationNameChange);
        if(XSSFileDetection.isMaliciousCodeInFile(proofOfQualificationNameChange)) {
            throw new InvalidFileUploadException();
        }}
        HpProfile hpProfile=hpProfileDaoService.findById(hpProfileId);
        HpProfile latestHpProfile = iHpProfileRepository.findLatestHpProfileFromWorkFlow(hpProfile.getRegistrationId());
        if(latestHpProfile!=null){
            hpProfile=latestHpProfile;
        }
        if (hpProfile.getNmrId() == null || !HpProfileStatus.APPROVED.getId().equals(hpProfile.getHpProfileStatus().getId())) {
            throw new WorkFlowException(NMRError.WORK_FLOW_EXCEPTION.getCode(), NMRError.WORK_FLOW_EXCEPTION.getMessage());
        }

        if(!iWorkFlowService.isAnyActiveWorkflowWithOtherApplicationType(hpProfileId,List.of(ApplicationType.ADDITIONAL_QUALIFICATION.getId()))){
            throw new WorkFlowException(NMRError.WORK_FLOW_CREATION_FAIL.getCode(), NMRError.WORK_FLOW_CREATION_FAIL.getMessage());
        }
        List<String> existingQualifications = iQualificationDetailRepository.getListOfQualificationByUserID(hpProfile.getUser().getId());
        validateQualificationDetailsAndProofs(qualificationDetailRequestTOs, proofs, existingQualifications);

        for (QualificationDetailRequestTO qualificationDetailRequestTO : qualificationDetailRequestTOs) {
            String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.ADDITIONAL_QUALIFICATION.getId()));
            WorkFlowRequestTO workFlowRequestTO = new WorkFlowRequestTO();
            workFlowRequestTO.setRequestId(requestId);
            workFlowRequestTO.setActionId(Action.SUBMIT.getId());
            workFlowRequestTO.setActorId(Group.HEALTH_PROFESSIONAL.getId());
            workFlowRequestTO.setApplicationTypeId(ApplicationType.ADDITIONAL_QUALIFICATION.getId());
            workFlowRequestTO.setHpProfileId(hpProfileId);
            qualificationDetailRequestTO.setRequestId(requestId);
            iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
        }
        hpProfileDaoService.saveQualificationDetails(hpProfileDaoService.findById(hpProfileId), null, qualificationDetailRequestTOs, proofs, proofOfQualificationNameChange);

        return SUCCESS_RESPONSE;
    }

    @Transactional
    @Override
    public String updateQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs, List<MultipartFile> proofs) throws InvalidRequestException, WorkFlowException, IOException, InvalidFileUploadException {
        if(proofs!=null) {
            for (MultipartFile file : proofs) {
                isFileTypeSupported(file);
                if(XSSFileDetection.isMaliciousCodeInFile(file)) {
        			throw new InvalidFileUploadException();
                }
            }
        }
        for (QualificationDetailRequestTO requestTO : qualificationDetailRequestTOs) {
            WorkFlow lastWorkFlowForHealthProfessional = workFlowRepository.findByRequestId(requestTO.getRequestId());
            if (lastWorkFlowForHealthProfessional != null && WorkflowStatus.QUERY_RAISED.getId().equals(lastWorkFlowForHealthProfessional.getWorkFlowStatus().getId())) {
                hpProfileDaoService.saveQualificationDetails(hpProfileDaoService.findById(hpProfileId), null, qualificationDetailRequestTOs, proofs, null);
                iWorkFlowService.assignQueriesBackToQueryCreator(requestTO.getRequestId(),hpProfileId);
                iQueriesService.markQueryAsClosed(requestTO.getRequestId());
            } else {
                return FAILURE_RESPONSE;
            }
        }
        return SUCCESS_RESPONSE;
    }

    @Override
    public HpProfilePersonalResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
                                                                   HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException {
        log.info("In HpRegistrationServiceImpl : addOrUpdateHpPersonalDetail method");
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpPersonalDetails(hpProfileId, hpPersonalUpdateRequestTO);

        log.debug("Update Successful. Calling the getHealthProfessionalPersonalDetail method to retrieve the Details. ");
        HpProfilePersonalResponseTO healthProfessionalPersonalDetail = getHealthProfessionalPersonalDetail(hpProfileUpdateResponseTO.getHpProfileId());

        log.info("HpRegistrationServiceImpl: addOrUpdateHpPersonalDetail method: Execution Successful. ");
        return healthProfessionalPersonalDetail;

    }

    @Override
    public HpProfileRegistrationResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
                                                                           HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, MultipartFile registrationCertificate, List<MultipartFile> degreeCertificate, MultipartFile proofOfQualificationNameChange, MultipartFile proofOfRegistrationNameChange) throws InvalidRequestException, NmrException {
        log.info("In HpRegistrationServiceImpl : addOrUpdateHpRegistrationDetail method");
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpRegistrationDetails(hpProfileId, hpRegistrationUpdateRequestTO, registrationCertificate, degreeCertificate,proofOfQualificationNameChange, proofOfRegistrationNameChange);

        log.debug("Update Successful. Calling the getHealthProfessionalRegistrationDetail method to retrieve the Details. ");
        HpProfileRegistrationResponseTO healthProfessionalRegistrationDetail = getHealthProfessionalRegistrationDetail(hpProfileUpdateResponseTO.getHpProfileId());

        log.info("HpRegistrationServiceImpl: addOrUpdateHpRegistrationDetail method: Execution Successful. ");
        return healthProfessionalRegistrationDetail;
    }

    @Override
    public HpProfileWorkDetailsResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
                                                                       HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<MultipartFile> proofs) throws NmrException, InvalidRequestException, NotFoundException {

        log.info("In HpRegistrationServiceImpl : addOrUpdateWorkProfileDetail method");

        log.debug("WorkProfileDetails Validation Successful. Calling the updateWorkProfileDetails method to update the work profile details. ");
        validateUserAccessToResourceForHP(hpProfileId);
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateWorkProfileDetails(hpProfileId, hpWorkProfileUpdateRequestTO, proofs);

        log.debug("Update Successful. Calling the getHealthProfessionalRegistrationDetail method to retrieve the work profile details. ");
        HpProfileWorkDetailsResponseTO healthProfessionalWorkDetail = getHealthProfessionalWorkDetail(hpProfileUpdateResponseTO.getHpProfileId());

        log.info("HpRegistrationServiceImpl: addOrUpdateWorkProfileDetail method: Execution Successful. ");
        return healthProfessionalWorkDetail;

    }

    @Transactional
    @Override
    public HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO)
            throws InvalidRequestException, WorkFlowException {

        log.info("In HpRegistrationServiceImpl : submitHpProfile method");
        String requestId = null;
        if (hpSubmitRequestTO.getHpProfileId() != null &&
                iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpSubmitRequestTO.getHpProfileId())) {
            throw new WorkFlowException(NMRError.WORK_FLOW_CREATION_FAIL.getCode(), NMRError.WORK_FLOW_CREATION_FAIL.getMessage());
        }
        WorkFlow lastWorkFlowForHealthProfessional = workFlowRepository.findLastWorkFlowForHealthProfessional(hpSubmitRequestTO.getHpProfileId());
        if (lastWorkFlowForHealthProfessional != null && WorkflowStatus.QUERY_RAISED.getId().equals(lastWorkFlowForHealthProfessional.getWorkFlowStatus().getId())) {
            log.debug("Calling assignQueriesBackToQueryCreator method since there is an existing workflow with 'Query Raised' work flow status. ");
            iQueriesService.markQueryAsClosed(lastWorkFlowForHealthProfessional.getRequestId());
            HpProfile hpProfile = hpProfileDaoService.findById(hpSubmitRequestTO.getHpProfileId());
            hpProfile.setTransactionId(hpSubmitRequestTO.getTransactionId());
            hpProfile.setESignStatus(ESignStatus.QUERY_RESOLVED_PROFILE_NOT_ESIGNED.getId());
            if(ApplicationType.HP_REGISTRATION.getId().equals(hpSubmitRequestTO.getApplicationTypeId()) || ApplicationType.FOREIGN_HP_REGISTRATION.getId().equals(hpSubmitRequestTO.getApplicationTypeId())) {
                hpProfile.setHpProfileStatus(in.gov.abdm.nmr.entity.HpProfileStatus.builder().id(HpProfileStatus.PENDING.getId()).build());
            }
            iHpProfileRepository.save(hpProfile);
        } else {
            log.debug("Proceeding to submit the profile since request_id is not given as a part of input payload");
            requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(hpSubmitRequestTO.getApplicationTypeId()));
            log.debug("New Request id is built - " + requestId);
            WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
                    .applicationTypeId(hpSubmitRequestTO.getApplicationTypeId())
                    .hpProfileId(hpSubmitRequestTO.getHpProfileId())
                    .actionId(Action.SUBMIT.getId())
                    .actorId(Group.HEALTH_PROFESSIONAL.getId())
                    .build();
            log.debug("Initiating Submission Workflow");
            iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);

            log.debug("Updating the request_id, e-sign status and transaction id in hp_profile table");
            HpProfile hpProfileById = iHpProfileRepository.findHpProfileById(hpSubmitRequestTO.getHpProfileId());
            if(hpSubmitRequestTO.getApplicationTypeId().equals(ApplicationType.HP_REGISTRATION.getId())) {
                hpProfileById.setTransactionId(hpSubmitRequestTO.getTransactionId());
                hpProfileById.setESignStatus(hpSubmitRequestTO.getESignStatus() != null ? hpSubmitRequestTO.getESignStatus() : ESignStatus.PROFILE_NOT_ESIGNED.getId());
            }
            if(hpSubmitRequestTO.getApplicationTypeId().equals(ApplicationType.HP_MODIFICATION.getId())){
                hpProfileById.setModTransactionId(hpSubmitRequestTO.getTransactionId());
                hpProfileById.setModESignStatus(hpSubmitRequestTO.getESignStatus() != null ? hpSubmitRequestTO.getESignStatus() : ESignStatus.PROFILE_NOT_ESIGNED.getId());
            }
            hpProfileById.setRequestId(requestId);
            hpProfileById.setConsent(hpSubmitRequestTO.getHprShareAcknowledgement() != null ? hpSubmitRequestTO.getHprShareAcknowledgement() : NO);
            if(ApplicationType.FOREIGN_HP_REGISTRATION.getId() .equals(hpSubmitRequestTO.getApplicationTypeId()) || ApplicationType.HP_REGISTRATION.getId().equals(hpSubmitRequestTO.getApplicationTypeId())) {
                hpProfileById.setHpProfileStatus(in.gov.abdm.nmr.entity.HpProfileStatus.builder().id(HpProfileStatus.PENDING.getId()).build());
            }
            log.debug("Updating the request_id in registration_details table");
            RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpSubmitRequestTO.getHpProfileId());
            registrationDetails.setRequestId(requestId);

            registrationDetailRepository.save(registrationDetails);
            iHpProfileRepository.save(hpProfileById);

            List<QualificationDetails> qualificationDetailsList = new ArrayList<>();
            if (hpSubmitRequestTO.getApplicationTypeId().equals(ApplicationType.HP_REGISTRATION.getId())) {
                log.debug("Updating the request_id in qualification_details table");
                List<QualificationDetails> qualificationDetails = qualificationDetailRepository.getQualificationDetailsByUserId(hpProfileById.getUser().getId());
                String finalRequestId1 = requestId;
                qualificationDetails.forEach(qualifications -> {
                    qualifications.setRequestId(finalRequestId1);
                    qualificationDetailsList.add(qualifications);
                });
                qualificationDetailRepository.saveAll(qualificationDetailsList);
            }
            List<ForeignQualificationDetails> foreignQualificationDetails = new ArrayList<>();
            if (hpSubmitRequestTO.getApplicationTypeId().equals(ApplicationType.FOREIGN_HP_REGISTRATION.getId())) {
                log.debug("Updating the request_id in foreign_qualification_details table");
                List<ForeignQualificationDetails> qualificationDetails = customQualificationDetailRepository.getQualificationDetailsByUserId(hpProfileById.getUser().getId());
                String finalRequestId1 = requestId;
                qualificationDetails.forEach(qualifications -> {
                    qualifications.setRequestId(finalRequestId1);
                    foreignQualificationDetails.add(qualifications);
                });
                customQualificationDetailRepository.saveAll(foreignQualificationDetails);
            }
        }

        log.info("HpRegistrationServiceImpl: submitHpProfile method: Execution Successful. ");
        return new HpProfileAddResponseTO(201, "Hp Profile Submitted Successfully!", hpSubmitRequestTO.getHpProfileId(), requestId);
    }

    public void validateUserAccessToResource(BigInteger hpProfileId) {
        User loggedInUser = accessControlService.getLoggedInUser();
        if (UserTypeEnum.HEALTH_PROFESSIONAL.getId().equals(loggedInUser.getUserType().getId())) {
            HpProfile hpProfile = iHpProfileRepository.findLatestEntryByUserid(loggedInUser.getId());
            if (!hpProfile.getId().equals(hpProfileId)) {
                log.error("Access denied: You do not have permissions to access this resource.");
                throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
            }
        } else if (UserTypeEnum.SMC.getId().equals(loggedInUser.getUserType().getId())) {
             if ("FALSE".equalsIgnoreCase(iRegistrationDetailRepository.isHPBelongsToLoggedInSMC(loggedInUser.getId(), hpProfileId))) {
                log.error("Access denied: HP does not belong to the login SMC.");
                throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
            }
        } else if (UserTypeEnum.COLLEGE.getId().equals(loggedInUser.getUserType().getId())) {
            CollegeProfile college = collegeProfileRepository.isHPBelongsToLoggedInCollege(loggedInUser.getId(), hpProfileId);
            if (college == null) {
                log.error("Access denied: HP does not belong to the login College.");
                throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
            }
        } else if (UserTypeEnum.NBE.getId().equals(loggedInUser.getUserType().getId())) {
            ForeignQualificationDetails foreignQualification = iCustomQualificationDetailRepository.isHPBelongsToLoggedInNBE(hpProfileId);
            if (foreignQualification == null) {
                log.error("Access denied: HP does not belong to the login NBE.");
                throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
            }
        }
    }

    @Override
    public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(BigInteger hpProfileId) {
        validateUserAccessToResource(hpProfileId);
        HpProfile hpProfile = hpProfileDaoService.findById(hpProfileId);
        in.gov.abdm.nmr.entity.Address communicationAddressByHpProfileId = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, AddressType.COMMUNICATION.getId());
        in.gov.abdm.nmr.entity.Address kycAddressByHpProfileId = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, AddressType.KYC.getId());
        BigInteger applicationTypeId = null;
        BigInteger workFlowStatusId = null;
        String requestId = null;
        BigInteger hpProfileStatusId = null;
        HpProfile latestHpProfile = iHpProfileRepository.findLatestHpProfileFromWorkFlow(hpProfile.getRegistrationId());
        if (latestHpProfile != null) {
            WorkFlow workFlow = workFlowRepository.findLastWorkFlowForHealthProfessional(hpProfileId);
            hpProfileStatusId = latestHpProfile.getHpProfileStatus().getId();
            if (workFlow != null) {
                applicationTypeId = workFlow.getApplicationType().getId();
                workFlowStatusId = workFlow.getWorkFlowStatus().getId();
                requestId = workFlow.getRequestId();
            }
        } else {
            hpProfileStatusId = hpProfile.getHpProfileStatus().getId();
        WorkFlow workFlow = workFlowRepository.findLastWorkFlowForHealthProfessional(hpProfileId);
        if (workFlow != null) {
            applicationTypeId = workFlow.getApplicationType().getId();
            workFlowStatusId = workFlow.getWorkFlowStatus().getId();
            requestId = workFlow.getRequestId();
        }}
        HpProfilePersonalResponseTO hpProfilePersonalResponseTO = HpPersonalDetailMapper.convertEntitiesToPersonalResponseTo(hpProfile, communicationAddressByHpProfileId, kycAddressByHpProfileId, applicationTypeId, workFlowStatusId, requestId, hpProfileStatusId);
        TrackApplicationReadStatus trackApplicationReadStatus = iTrackApplicationReadStatusRepository.findByUserId(hpProfile.getUser().getId());
        if(trackApplicationReadStatus != null){
            hpProfilePersonalResponseTO.setIsTrackApplicationReadStatus(trackApplicationReadStatus.isReadStatus());
        }
        if(latestHpProfile!=null) {
            hpProfilePersonalResponseTO.setEsignStatus(NMRUtil.getDerivedESignStatus(latestHpProfile.getESignStatus(), latestHpProfile.getModESignStatus()));
        }
        return  hpProfilePersonalResponseTO;
    }

    @Override
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId){
        validateUserAccessToResource(hpProfileId);
        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = null;
        List<WorkProfile> workProfileList = new ArrayList<>();
        List<LanguagesKnown> languagesKnown = new ArrayList<>();
        List<BigInteger> languagesKnownIds = new ArrayList<>();
        Optional<HpProfile> hpProfileOptional = iHpProfileRepository.findById(hpProfileId);
        if (hpProfileOptional.isPresent()) {
            User user = hpProfileOptional.get().getUser();
            if (user != null) {
                BigInteger userId = user.getId();
                workProfileList = workProfileRepository.getWorkProfileDetailsByUserId(userId);
                languagesKnown = languagesKnownRepository.findByUserId(userId);
            }
        }

        if (languagesKnown != null && !languagesKnown.isEmpty()) {
            languagesKnownIds = languagesKnown.stream().map(languageKnown -> {
                Language language = languageKnown.getLanguage();
                return language != null ? language.getId() : null;
            }).toList();
        }

        if (!workProfileList.isEmpty()) {
            hpProfileWorkDetailsResponseTO = HpProfileWorkProfileMapper.convertEntitiesToWorkDetailResponseTo(workProfileList);
            hpProfileWorkDetailsResponseTO.setLanguagesKnownIds(languagesKnownIds);
        }
        return hpProfileWorkDetailsResponseTO;
    }

    @Override
    public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(BigInteger hpProfileId) {
        validateUserAccessToResource(hpProfileId);
        RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
        HpNbeDetails nbeDetails = hpNbeDetailsRepository.findByUserId(registrationDetails.getHpProfileId().getUser().getId());
        List<QualificationDetails> indianQualifications = qualificationDetailRepository.getQualificationDetailsByUserId(registrationDetails.getHpProfileId().getUser().getId());
        List<ForeignQualificationDetails> internationalQualifications = customQualificationDetailRepository.getQualificationDetailsByUserId(registrationDetails.getHpProfileId().getUser().getId());
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = hpProfileRegistrationMapper.convertEntitiesToRegistrationResponseTo(registrationDetails, nbeDetails, indianQualifications, internationalQualifications);
        hpProfileRegistrationResponseTO.setHpProfileId(hpProfileId);
        for(QualificationDetailResponseTo qualificationDetails:hpProfileRegistrationResponseTO.getQualificationDetailResponseTos()){
            qualificationDetails.setQueries(queriesRepository.findOpenQueriesByRequestId(qualificationDetails.getRequestId()));
        }
        return hpProfileRegistrationResponseTO;
    }

    @Transactional
    public KycResponseMessageTo userKycFuzzyMatch(List<Council> councils ,String registrationNumber, BigInteger councilId, String name, String gender, java.sql.Date dob) throws ParseException {

        Council council = councils.isEmpty() ? null : councils.get(0);

        if (council != null) {

            List<FuzzyParameter> fuzzyParameters = new ArrayList<>();
            double nameMatch = getFuzzyScore(council.getFullName(), name);
            boolean isNameMatching = nameMatch > NMRConstants.FUZZY_MATCH_LIMIT;
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_NAME, council.getFullName(), name, isNameMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));

            double genderMatch = getFuzzyScore(council.getGender(), gender);
            boolean isGenderMatching = genderMatch > NMRConstants.FUZZY_MATCH_LIMIT;
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_GENDER, council.getGender(), gender, isGenderMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));

            SimpleDateFormat kycDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat imrDateFormat = new SimpleDateFormat("MM/dd/yy");

            boolean isDobMatching;
            try {
                Date dateOfBirth = imrDateFormat.parse(council.getDateOfBirth());
                isDobMatching = (kycDateFormat.format(dateOfBirth).compareTo(kycDateFormat.format(dob))) == 0;
            } catch (ParseException e) {
                log.info("Exception occurred while parsing dob" + e.getMessage());
                isDobMatching = false;
            }
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_DOB, council.getDateOfBirth(), dob.toString(), isDobMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));
            if (isNameMatching && isDobMatching && isGenderMatching) {
                return new KycResponseMessageTo(fuzzyParameters, NMRConstants.SUCCESS_RESPONSE);
            } else {
                return new KycResponseMessageTo(fuzzyParameters, NMRConstants.FAILURE_RESPONSE);
            }
        } else {
            return new KycResponseMessageTo(Collections.emptyList(), NMRConstants.USER_NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public ResponseMessageTo addNewHealthProfessional(NewHealthPersonalRequestTO request) throws DateException, ParseException, GeneralSecurityException, InvalidRequestException, NmrException {

        if (request.getUsername()!=null && userDaoService.existsByUserNameAndUserTypeId(request.getUsername(), UserTypeEnum.HEALTH_PROFESSIONAL.getId())) {
            throw new InvalidRequestException(NMRError.USERNAME_ALREADY_REGISTERED.getCode(),NMRError.USERNAME_ALREADY_REGISTERED.getMessage());
        }

        if (request.getMobileNumber()!=null && userDaoService.existsByMobileNumberAndUserTypeId(request.getMobileNumber(), UserTypeEnum.HEALTH_PROFESSIONAL.getId())) {
            throw new InvalidRequestException(NMRError.MOBILE_NUM_ALREADY_REGISTERED.getCode(),NMRError.MOBILE_NUM_ALREADY_REGISTERED.getMessage());
        }

        if (request.getEmail() != null && userDaoService.existsByEmailAndUserTypeId(request.getEmail(), UserTypeEnum.HEALTH_PROFESSIONAL.getId())) {
            throw new InvalidRequestException(NMRError.EMAIL_ID_ALREADY_REGISTERED.getCode(),NMRError.EMAIL_ID_ALREADY_REGISTERED.getMessage());
        }

        String hashedPassword = bCryptPasswordEncoder.encode(rsaUtil.decrypt(request.getPassword()));
        User userDetail = new User(null, request.getEmail(), request.getMobileNumber(), null, hashedPassword, null, true, false,

                entityManager.getReference(UserType.class, UserTypeEnum.HEALTH_PROFESSIONAL.getId()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE_ADMIN.getId()), entityManager.getReference(UserGroup.class, Group.HEALTH_PROFESSIONAL.getId()), true, 0, null, request.getUsername(), request.getHprId(), request.getHprIdNumber(), request.isNew(), false, false, null);
        userDaoService.save(userDetail);
        Password password = new Password(null, hashedPassword, userDetail);
        passwordDaoService.save(password);

        StateMedicalCouncil stateMedicalCouncil = stateMedicalCouncilRepository.findStateMedicalCouncilById(BigInteger.valueOf(Long.parseLong(request.getSmcId())));

        List<Council> imrRecords = councilService.getCouncilByRegistrationNumberAndCouncilName(request.getRegistrationNumber(), stateMedicalCouncil.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        KycResponseMessageTo kycResponseMessageTo = userKycFuzzyMatch(imrRecords, request.getRegistrationNumber(), new BigInteger(request.getSmcId()), request.getName(), request.getGender(), java.sql.Date.valueOf(sdf2.format(sdf.parse(request.getBirthdate()))));

        Council imrProfileDetails = imrRecords.isEmpty() ? null : imrRecords.get(0);
        RegistrationsDetails imrRegistrationsDetails = null;
        List<QualificationsDetails> qualificationDetailsList = new ArrayList<>();

        if (imrProfileDetails != null && SUCCESS_RESPONSE.equalsIgnoreCase(kycResponseMessageTo.getKycFuzzyMatchStatus())) {
            imrRegistrationsDetails = imrProfileDetails.getRegistrationsDetails().isEmpty() ? null : imrProfileDetails.getRegistrationsDetails().get(0);
            for (QualificationsDetails qualificationsDetails : imrRegistrationsDetails.getQualificationsDetails()) {
                if (qualificationsDetails.getName() != null
                        && (MBBS_PATTERN_MATCHER.matcher(qualificationsDetails.getName()).matches()
                        || MBBS_QUALIFICAITON_NAMES.stream().anyMatch(qualificationsDetails.getName()::equalsIgnoreCase))) {
                    qualificationDetailsList.add(qualificationsDetails);
                }
            }
        }

        HpProfile hpProfile = new HpProfile();
        hpProfile.setAadhaarToken(request.getAadhaarToken() != null ? request.getAadhaarToken() : null);
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            java.util.Date dt = df.parse(request.getBirthdate());
            hpProfile.setDateOfBirth(new java.sql.Date(dt.getTime()));
        } catch (ParseException e) {
            throw new DateException(NMRError.DATE_EXCEPTION.getCode(), NMRError.DATE_EXCEPTION.getMessage());
        }

        hpProfile.setEmailId(request.getEmail() != null ? request.getEmail() : null);
        Name splitName = splitName(request.getName());
        hpProfile.setFirstName(splitName.getFirstName() != null ? splitName.getFirstName() : "");
        hpProfile.setMiddleName(splitName.getMiddleName() != null ? splitName.getMiddleName() : "");
        hpProfile.setLastName(splitName.getLastName() != null ? splitName.getLastName() : "");
        hpProfile.setFullName(request.getName());
        hpProfile.setGender(request.getGender());
        hpProfile.setMobileNumber(request.getMobileNumber() != null ? request.getMobileNumber() : null);
        hpProfile.setSalutation(NMRConstants.SALUTATION_DR);
        hpProfile.setProfilePhoto(request.getPhoto() != null ? new String(Base64.getDecoder().decode(request.getPhoto())) : null);
        hpProfile.setRegistrationId(request.getRegistrationNumber());
        hpProfile.setIsSameAddress(String.valueOf(false));
        hpProfile.setCountryNationality(countryRepository.findByName(NMRConstants.DEFAULT_COUNTRY_AADHAR));
        hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(HpProfileStatus.DRAFT.getId()).get());
        hpProfile.setIsNew(SUCCESS_RESPONSE.equalsIgnoreCase(kycResponseMessageTo.getKycFuzzyMatchStatus()) ? NO : YES);
        hpProfile.setUser(userDetail);
        hpProfile.setESignStatus(ESignStatus.PROFILE_NOT_ESIGNED.getId());
        hpProfile = iHpProfileRepository.save(hpProfile);

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setStateMedicalCouncil(stateMedicalCouncil);
        registrationDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetails.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetails.setHpProfileId(hpProfile);
        registrationDetails.setRegistrationNo(request.getRegistrationNumber());

        if (imrRegistrationsDetails != null && SUCCESS_RESPONSE.equalsIgnoreCase(kycResponseMessageTo.getKycFuzzyMatchStatus())) {
            boolean isRenewable = imrRegistrationsDetails.isRenewableRegistration();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
            if (isRenewable) {
                registrationDetails.setIsRenewable("1");
                registrationDetails.setRenewableRegistrationDate(simpleDateFormat.parse(imrRegistrationsDetails.getRenewableRegistrationDate() != null ?
                        imrRegistrationsDetails.getRenewableRegistrationDate() : null));

            } else {
                registrationDetails.setIsRenewable("0");
                registrationDetails.setRenewableRegistrationDate(null);
            }
            registrationDetails.setRegistrationDate(imrRegistrationsDetails.getRegistrationDate() !=null ? simpleDateFormat.parse(imrRegistrationsDetails.getRegistrationDate()): null);
        }
        iRegistrationDetailRepository.save(registrationDetails);

        if (!qualificationDetailsList.isEmpty()) {
            QualificationsDetails qualificationsDetails = qualificationDetailsList.get(0);

            QualificationDetails qualificationDetails = new QualificationDetails();
            qualificationDetails.setHpProfile(hpProfile);
            qualificationDetails.setQualificationYear(qualificationsDetails.getQualificationYear() != null ? qualificationsDetails.getQualificationYear() : null);
            qualificationDetails.setQualificationMonth(qualificationsDetails.getQualificationMonth() != null ? qualificationsDetails.getQualificationMonth() : null);
            qualificationDetails.setCollege(qualificationsDetails.getCollege() != null ? iCollegeMasterRepository.getCollegeByName(qualificationsDetails.getCollege()) : null);
            qualificationDetails.setSystemOfMedicine(qualificationsDetails.getSystemOfMedicine() != null ? qualificationsDetails.getSystemOfMedicine() : null);
            qualificationDetails.setCountry(qualificationsDetails.getCountry() != null ? countryRepository.findByName(qualificationsDetails.getCountry()) : null);
            qualificationDetails.setCourse(qualificationsDetails.getCourse() != null ? courseRepository.getByCourseName(qualificationsDetails.getCourse()) : null);
            qualificationDetails.setState(qualificationsDetails.getState() != null ? stateRepository.findByName(qualificationsDetails.getState()) : null);
            qualificationDetails.setName(qualificationsDetails.getName() != null ? qualificationsDetails.getName() : null);
            String university = qualificationsDetails.getUniversity();
            if (university != null && qualificationDetails.getCollege() != null && qualificationDetails.getCollege().getId() != null) {
                UniversityMaster universityMaster = universityMasterRepository.findUniversityByName(university,qualificationDetails.getCollege().getId());
                qualificationDetails.setUniversity(universityMaster);
            }
            qualificationDetails.setRegistrationDetails(registrationDetails);
            qualificationDetails.setUser(userDetail);
            qualificationDetails.setIsVerified(QUALIFICATION_STATUS_PENDING);
            iQualificationDetailRepository.save(qualificationDetails);
        }

        in.gov.abdm.nmr.entity.Address kycAddress = new in.gov.abdm.nmr.entity.Address();
            kycAddress.setPincode(request.getPincode());
        kycAddress.setMobile(request.getMobileNumber());
        kycAddress.setAddressLine1(request.getAddress());
        kycAddress.setEmail(request.getEmail() != null ? request.getEmail() : null);
        kycAddress.setHouse(request.getHouse() != null ? request.getHouse() : null);
        kycAddress.setStreet(request.getStreet() != null ? request.getStreet() : null);
        kycAddress.setLocality(request.getLocality() != null ? request.getLocality() : null);
        kycAddress.setHouse(request.getHouse() != null ? request.getHouse() : null);
        kycAddress.setLandmark(request.getLandmark() != null ? request.getLandmark() : null);
        kycAddress.setHpProfileId(hpProfile.getId());
        kycAddress.setAddressTypeId(new in.gov.abdm.nmr.entity.AddressType(AddressType.KYC.getId(), AddressType.KYC.name()));
        kycAddress.setVillage((request.getLocality()!=null && request.getSubDist()!=null)  ? villagesRepository.getVillageByNameAndDistrictName(request.getLocality(), request.getSubDist()) : null);
        kycAddress.setSubDistrict(request.getSubDist() != null ? subDistrictRepository.findByName(request.getSubDist()) : null);
        kycAddress.setState(stateRepository.findByName(request.getState().toUpperCase()));
        if (kycAddress.getState() != null) {
            kycAddress.setDistrict(districtRepository.findByDistrictNameAndStateId(request.getDistrict().toUpperCase(), kycAddress.getState().getId()));
        }
        kycAddress.setCountry(stateRepository.findByName(request.getState().toUpperCase()).getCountry());

        if (imrProfileDetails != null && SUCCESS_RESPONSE.equalsIgnoreCase(kycResponseMessageTo.getKycFuzzyMatchStatus())) {
            in.gov.abdm.nmr.entity.Address communicationAddressEntity = new in.gov.abdm.nmr.entity.Address();
            Address communicationAddress = new Address();
            for (Address address : imrProfileDetails.getAddress()) {
                if (address.getType().equalsIgnoreCase(AddressType.COMMUNICATION.name())) {
                    communicationAddress = address;
                }
            }
            if (communicationAddress != null) {
                communicationAddressEntity.setPincode(communicationAddress.getPincode() != null ? communicationAddress.getPincode() : null);
                communicationAddressEntity.setMobile(imrProfileDetails.getMobileNumber() != null ? imrProfileDetails.getMobileNumber() : null);
                communicationAddressEntity.setAddressLine1(communicationAddress.getAddressLine1() != null ? communicationAddress.getAddressLine1() : null);
                communicationAddressEntity.setEmail(imrProfileDetails.getEmail() != null ? imrProfileDetails.getEmail() : null);
                communicationAddressEntity.setHouse(null);
                communicationAddressEntity.setStreet(null);
                communicationAddressEntity.setLocality(null);
                communicationAddressEntity.setLandmark(null);
                communicationAddressEntity.setHpProfileId(hpProfile.getId());
                communicationAddressEntity.setAddressTypeId(new in.gov.abdm.nmr.entity.AddressType(AddressType.COMMUNICATION.getId(), AddressType.COMMUNICATION.name()));
                communicationAddressEntity.setVillage(communicationAddress.getCity() != null && communicationAddress.getSubDistricts() != null ? villagesRepository.getVillageByNameAndDistrictName(communicationAddress.getCity(), communicationAddress.getSubDistricts()) : null);
                communicationAddressEntity.setSubDistrict(communicationAddress.getSubDistricts() != null ? subDistrictRepository.findByName(communicationAddress.getSubDistricts()) : null);
                communicationAddressEntity.setState(communicationAddress.getState() != null ? stateRepository.findByName(communicationAddress.getState().toUpperCase()) : null);
                communicationAddressEntity.setDistrict(communicationAddress.getDistrict() != null && communicationAddressEntity.getState() != null ? districtRepository.findByDistrictNameAndStateId(communicationAddress.getDistrict().toUpperCase(), communicationAddressEntity.getState().getId()) : null);
                communicationAddressEntity.setCountry(communicationAddress.getCountry() != null ? countryRepository.findByName(communicationAddress.getCountry()) : null);

                iAddressRepository.saveAll(List.of(kycAddress, communicationAddressEntity));
            } else {
                iAddressRepository.saveAll(List.of(kycAddress));
            }

        } else {
            iAddressRepository.saveAll(List.of(kycAddress));
        }
        try {
            notificationService.sendNotificationForAccountCreation(request.getUsername(), request.getMobileNumber());
        } catch (Exception exception) {
            log.debug("error occurred while sending notification:" + exception.getLocalizedMessage());
        }
        return new ResponseMessageTo(SUCCESS_RESPONSE);
    }


    private Name splitName(String fullName) throws NmrException {
        log.debug("processing splitting fullName: {} ", fullName);
        Name splitName = new Name();
        if (!StringUtils.isEmpty(fullName)) {
            List<String> name = new ArrayList<>(Arrays.asList(fullName.split(" ")));
            if (name.size() == 1) {
                splitName.setFirstName(name.get(0));
            } else if (name.size() == 2) {
                splitName.setFirstName(name.get(0));
                splitName.setLastName(name.get(1));
            } else {
                int idx = fullName.lastIndexOf(' ');
                String firstNameTemp = fullName.substring(0, idx);
                try {
                    int idxF = firstNameTemp.lastIndexOf(' ');
                    splitName.setFirstName(firstNameTemp.substring(0, idxF));
                    splitName.setMiddleName(firstNameTemp.substring(idxF + 1));
                } catch (IndexOutOfBoundsException e) {
                    log.error("Error splitting fullName: {} Detail exception is {}", fullName, e);
                    throw new NmrException();
                }
                splitName.setLastName(fullName.substring(idx + 1));
            }
        }
        return splitName;
    }


    @Override
    public void updateHealthProfessionalEmailMobile(BigInteger hpProfileId, HealthProfessionalPersonalRequestTo request) throws OtpException, InvalidRequestException {

        String transactionId = request.getTransactionId();
        HpProfile hpProfile = iHpProfileRepository.findHpProfileById(hpProfileId);
        if (request.getMobileNumber() != null) {
            if (transactionId == null) {
                throw new InvalidRequestException(NMRError.MISSING_MANDATORY_FIELD.getCode(), NMRError.MISSING_MANDATORY_FIELD.getMessage());
            } else {
                if (otpValidationService.isOtpVerified(transactionId)) {
                    throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage());
                }
            }
        }
        if(request.getMobileNumber()!=null) {

            if (hpProfile != null && !hpProfile.getUser().getMobileNumber().equals(request.getMobileNumber())) {
                if (!(userDaoService.checkMobileUsedByOtherUser(hpProfile.getUser().getId(), request.getMobileNumber(), UserTypeEnum.HEALTH_PROFESSIONAL.getId()))) {
                    hpProfile.getUser().setMobileNumber(request.getMobileNumber());
                    hpProfile.setMobileNumber(request.getMobileNumber());
                    iHpProfileRepository.save(hpProfile);
                } else {
                    throw new InvalidRequestException(NMRConstants.MOBILE_USED_BY_OTHER_USER);
                }
            } else {
                throw new InvalidRequestException(UPDATING_SAME_MOBILE_NUMBER);

            }
        }

        String eSignTransactionId = request.getESignTransactionId();
        if (eSignTransactionId != null && !eSignTransactionId.isBlank() && hpProfile != null) {
            if(hpProfile.getModTransactionId()==null) {
                hpProfile.setTransactionId(eSignTransactionId);
            }else {
                hpProfile.setModTransactionId(eSignTransactionId);
            }
            iHpProfileRepository.save(hpProfile);
        }

        BigInteger masterHpProfileId = iHpProfileRepository.findMasterHpProfileByHpProfileId(hpProfileId);
        if (masterHpProfileId != null) {
            if (request.getMobileNumber() != null) {
                iHpProfileMasterRepository.updateMasterHpProfileMobile(masterHpProfileId, request.getMobileNumber());
            }
            if (eSignTransactionId != null && !eSignTransactionId.isBlank()) {
                HpProfileMaster hpProfileMaster = iHpProfileMasterRepository.findHpProfileMasterById(masterHpProfileId);
                if (hpProfileMaster != null) {
                    hpProfileMaster.setTransactionId(eSignTransactionId);
                    iHpProfileMasterRepository.save(hpProfileMaster);
                }

            }
        }

        if(hpProfile != null && Boolean.TRUE == request.getTrackApplicationReadStatus()){
            TrackApplicationReadStatus trackApplicationReadStatus = iTrackApplicationReadStatusRepository.findByUserId(hpProfile.getUser().getId());
            trackApplicationReadStatus.setReadStatus(true);
            iTrackApplicationReadStatusRepository.save(trackApplicationReadStatus);
        }
    }

    /**
     * Creates new unique token for reset password transaction
     *
     * @param verifyEmailLinkTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo getEmailVerificationLink(BigInteger hpProfileId, VerifyEmailLinkTo verifyEmailLinkTo) {

        try {

            HpProfile hpProfile = iHpProfileRepository.findHpProfileById(hpProfileId);

            if (hpProfile != null) {

                if (!(hpProfile.getUser().isEmailVerified() && hpProfile.getUser().getEmail().equals(verifyEmailLinkTo.getEmail()))) {

                    if (!userDaoService.checkEmailUsedByOtherUser(hpProfile.getUser().getId(), verifyEmailLinkTo.getEmail(), hpProfile.getUser().getUserType().getId())) {

                        hpProfile.setEmailId(verifyEmailLinkTo.getEmail());
                        User user = userDaoService.findById(hpProfile.getUser().getId());
                        user.setEmail(verifyEmailLinkTo.getEmail());
                        user.setEmailVerified(false);
                        userDaoService.save(user);
                        iHpProfileRepository.save(hpProfile);
                        return notificationService.sendNotificationForEmailVerificationLink(verifyEmailLinkTo.getEmail(), generateLink(new SendLinkOnMailTo(verifyEmailLinkTo.getEmail(), hpProfile.getUser().getUserType().getId())));
                    } else {

                        return new ResponseMessageTo(NMRConstants.EMAIL_USED_BY_OTHER_USER);
                    }
                } else {
                    return new ResponseMessageTo(NMRConstants.EMAIL_ALREADY_VERIFIED);
                }
            } else {
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Something went wrong while sending email verification link.", e);
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }
    @Override
    public String generateLink(SendLinkOnMailTo sendLinkOnMailTo) {

        String token = RandomString.make(30);
        ResetToken resetToken = resetTokenRepository.findByUserNameAndUserType(sendLinkOnMailTo.getEmail(),sendLinkOnMailTo.getUserType());

        if (resetToken != null) {
            resetToken.setToken(token);
            resetToken.updateExpiryTime();
        } else {
            resetToken = new ResetToken(token, sendLinkOnMailTo.getEmail(),sendLinkOnMailTo.getUserType());
        }
        resetTokenRepository.save(resetToken);

        return emailVerifyUrl + "/user/verify-email?token=" + token;

    }

    @Override
    public void  delinkCurrentWorkDetails(WorkDetailsDelinkRequest workDetailsDelinkRequest) throws NmrException {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();

        User user = userDetailDaoService.findByUsername(userName, userType);
        if (Objects.isNull(user)) {
            throw new NmrException(USER_NOT_FOUND);
        } else {
            List<WorkProfile> activeWorkProfiles = workProfileRepository.getActiveWorkProfileDetailsByUserId(user.getId());
            List<String> activeFacilities = new ArrayList<>();
            activeWorkProfiles.forEach(workProfile -> activeFacilities.add(workProfile.getFacilityId()));
            workDetailsDelinkRequest.getFacilityId().retainAll(activeFacilities);
            if (!workDetailsDelinkRequest.getFacilityId().isEmpty()) {
                workProfileRepository.delinkWorkProfileDetailsByFacilityId(user.getId(), workDetailsDelinkRequest.getFacilityId());
            } else {
                throw new NmrException(DELINK_FAILED);
            }
        }
    }

    /**
     * This method implements the string comparsion logic based on the fuzzy
     * logic.
     *
     * @param s1 is the string which need to compare.
     * @param s2 is the string which needs to compare.
     * @return this will return the double value 0/1,0 means the strings are
     * not matched,
     * And 1 represents the strings are equal.
     */
    public double getFuzzyScore(String s1, String s2) {
        if (new Soundex().soundex(s1).equals(new Soundex().soundex(s2))) {
            log.info("Matched using soundex algorithm");
            return 100;
        }
        if (new Metaphone().isMetaphoneEqual(s1, s2)) {
            log.info("Matched using metaphone algorithm");
            return 100;
        }
        int maxLength = s2.length() > s1.length() ? s2.length() : s1.length();
        double levenshteinDistancePercentage = 100 - ((new LevenshteinDistance().apply(s1, s2) / (double) maxLength) * 100);
        if (levenshteinDistancePercentage >= NMRConstants.FUZZY_MATCH_LIMIT) {
            log.info("Matched using LevenshteinDistance algorithm");
            return levenshteinDistancePercentage;
        }
        int smiliarText = (similar(s1.toLowerCase(), s2.toLowerCase()) * 200) / (s1.length() + s2.length());
        if (smiliarText >= NMRConstants.FUZZY_MATCH_LIMIT) {
            log.info("Matched using similiar Text algorithm");
            return smiliarText;
        }
        return 0.0;
    }

    private int similar(String first, String second) {
        int p;
        int q;
        int l;
        int sum;
        int pos1 = 0;
        int pos2 = 0;
        int max = 0;
        char[] arr1 = first.toCharArray();
        char[] arr2 = second.toCharArray();
        int firstLength = arr1.length;
        int secondLength = arr2.length;

        for (p = 0; p < firstLength; p++) {
            for (q = 0; q < secondLength; q++) {
                for (l = 0; (p + l < firstLength) && (q + l < secondLength) && (arr1[p + l] == arr2[q + l]); l++) ;
                if (l > max) {
                    max = l;
                    pos1 = p;
                    pos2 = q;
                }

            }
        }
        sum = max;
        if (sum > 0) {
            if (pos1 > 0) {
                sum += this.similar(first.substring(0, Math.min(pos1, firstLength)), second.substring(0, Math.min(pos2, secondLength)));
            }

            if ((pos1 + max < firstLength) && (pos2 + max < secondLength)) {
                sum += this.similar(first.substring(pos1 + max, firstLength), second.substring(pos2 + max, secondLength));
            }
        }
        return sum;
    }
}
