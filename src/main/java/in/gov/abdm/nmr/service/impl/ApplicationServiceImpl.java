package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.minio.connector.service.S3ServiceImpl;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.ApplicationSubType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import in.gov.abdm.nmr.service.*;
import in.gov.abdm.nmr.util.NMRUtil;
import in.gov.abdm.nmr.util.XSSFileDetection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static in.gov.abdm.nmr.util.NMRConstants.*;
import static in.gov.abdm.nmr.util.NMRUtil.isFileTypeSupported;

/**
 * A class that implements all the methods of the interface  IActionService
 * which deals with the suspension and reactivation requests
 */
@Service
@Slf4j
public class ApplicationServiceImpl implements IApplicationService {

    /**
     * Injecting a IHpProfileRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IHpProfileRepository iHpProfileRepository;

    /**
     * Injecting a IRegistrationDetailRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IRegistrationDetailRepository iRegistrationDetailRepository;

    /**
     * Injecting a WorkProfileRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private WorkProfileRepository workProfileRepository;

    /**
     * Injecting a LanguagesKnownRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private LanguagesKnownRepository languagesKnownRepository;

    /**
     * Injecting a IQualificationDetailRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IQualificationDetailRepository iQualificationDetailRepository;

    /**
     * Injecting a IForeignQualificationDetailRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IForeignQualificationDetailRepository iForeignQualificationDetailRepository;

    /**
     * Injecting a IRequestCounterService bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IRequestCounterService requestCounterService;

    /**
     * Injecting a IWorkFlowService bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IWorkFlowService iWorkFlowService;

    /**
     * Injecting a IWorkFlowCustomRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IWorkFlowCustomRepository iWorkFlowCustomRepository;

    /**
     * Injecting a IFetchTrackApplicationDetailsCustomRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchTrackApplicationDetailsCustomRepository iFetchTrackApplicationDetailsCustomRepository;

    /**
     * Injecting a IUserRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IUserDaoService userDaoService;

    @Autowired
    private IAddressRepository addressRepository;

    @Autowired
    private IWorkFlowRepository iWorkFlowRepository;

    @Autowired
    private IWorkFlowAuditRepository iWorkFlowAuditRepository;

    @Value("${feature.toggle.minio.enable}")
    private boolean minioEnable;

    @Autowired
    private UserAttachmentsRepository userAttachmentsRepository;

    @Autowired
    S3ServiceImpl s3Service;

    @Autowired
    ISmcProfileRepository iSmcProfileRepository;
    @Autowired
    ICollegeProfileDaoService collegeProfileDaoService;

    @Autowired
    IHpRegistrationService iHpRegistrationService;

    @Autowired
    private IAccessControlService accessControlService;

    private static final Map<String, String> REACTIVATION_SORT_MAPPINGS = Map.of("id", " hp.id", "name", " hp.full_name", "createdAt", " wf.created_at", "reactivationDate", " wf.start_date", "suspensionType", " hp.hp_profile_status_id", "remarks", " wf.remarks");

    /**
     * This method is used to suspend a health professional based on the request provided.
     *
     * @param applicationRequestTo the request object containing necessary information to suspend a health professional.
     * @return a string indicating the result of the suspension request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    @Override
    public SuspendRequestResponseTo suspendRequest(ApplicationRequestTo applicationRequestTo) throws WorkFlowException, NmrException, InvalidRequestException {

        log.info("In ApplicationServiceImpl: suspendRequest method ");
        validateUserAccessToResourceForApplication(applicationRequestTo.getHpProfileId());
        HpProfile hpProfile = iHpProfileRepository.findHpProfileById(applicationRequestTo.getHpProfileId());
        HpProfile latestHpProfile = iHpProfileRepository.findLatestHpProfileFromWorkFlow(hpProfile.getRegistrationId());
        if (Objects.equals(HpProfileStatus.APPROVED.getId(), latestHpProfile.getHpProfileStatus().getId())) {
            log.debug("Building a new request_id");
            String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(applicationRequestTo.getApplicationTypeId()));
            initiateWorkFlow(applicationRequestTo, requestId, latestHpProfile);
            SuspendRequestResponseTo suspendRequestResponseTo = new SuspendRequestResponseTo();
            suspendRequestResponseTo.setProfileId(latestHpProfile.getId().toString());
            suspendRequestResponseTo.setMessage(SUCCESS_RESPONSE);

            log.info("ApplicationServiceImpl: suspendRequest method: Execution Successful. ");

            return suspendRequestResponseTo;
        } else {
            throw new WorkFlowException(NMRError.PROFILE_NOT_APPROVED.getCode(), NMRError.PROFILE_NOT_APPROVED.getMessage());
        }

    }

    /**
     * This method is used to reactivate a health professional based on the request provided.
     *
     * @param applicationRequestTo the request object containing necessary information to reactivate a health professional.
     * @return a string indicating the result of the reactivate request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    @Transactional
    @Override
    public ReactivateRequestResponseTo reactivateRequest(MultipartFile reactivationFile, ApplicationRequestTo applicationRequestTo) throws WorkFlowException, InvalidRequestException, IOException, InvalidFileUploadException {
        validateUserAccessToResourceForApplication(applicationRequestTo.getHpProfileId());
        log.info("In ApplicationServiceImpl: reactivateRequest method ");
        isFileTypeSupported(reactivationFile);
        if(XSSFileDetection.isMaliciousCodeInFile(reactivationFile)) {
            throw new InvalidFileUploadException();
        }
        HpProfile hpProfile = iHpProfileRepository.findHpProfileById(applicationRequestTo.getHpProfileId());
        HpProfile latestHpProfile = iHpProfileRepository.findLatestHpProfileFromWorkFlow(hpProfile.getRegistrationId());
        ReactivateRequestResponseTo reactivateRequestResponseTo = new ReactivateRequestResponseTo();
        if (!iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(latestHpProfile.getId())) {
            log.debug("Proceeding to Reactivate the profile since the profile is currently in Suspended / Black Listed state");

            if (Objects.equals(HpProfileStatus.SUSPENDED.getId(), latestHpProfile.getHpProfileStatus().getId())
                    || Objects.equals(HpProfileStatus.BLACKLISTED.getId(), latestHpProfile.getHpProfileStatus().getId())) {

                log.debug("Building Request id.");
                String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(applicationRequestTo.getApplicationTypeId()));
                WorkFlow workFlow = iWorkFlowRepository.findLastWorkFlowForHealthProfessionalForSuspension(latestHpProfile.getId());
                BigInteger loggedInUserGroupId = getGroupIdForLoggedInUser();
                if(Group.NMC.getId().equals(loggedInUserGroupId) || Group.SMC.getId().equals(loggedInUserGroupId)){
                    applicationRequestTo.setApplicationSubTypeId(ApplicationSubType.REACTIVATION_THROUGH_SMC.getId());
                    reactivateRequestResponseTo.setSelfReactivation(false);
                } else if (workFlow!=null && Group.NMC.getId().equals(workFlow.getPreviousGroup().getId())) {
                    log.debug("Proceeding to reactivate through SMC since the profile was suspended by NMC");
                    applicationRequestTo.setApplicationSubTypeId(ApplicationSubType.REACTIVATION_THROUGH_SMC.getId());
                    reactivateRequestResponseTo.setSelfReactivation(false);
                } else {
                    log.debug("Proceeding to initiate self reactivation since the profile wasn't suspended by NMC");
                    applicationRequestTo.setApplicationSubTypeId(ApplicationSubType.SELF_REACTIVATION.getId());
                    reactivateRequestResponseTo.setSelfReactivation(true);
                }
                initiateWorkFlow(applicationRequestTo, requestId, latestHpProfile);
                reactivateRequestResponseTo.setProfileId(latestHpProfile.getId().toString());
                reactivateRequestResponseTo.setMessage(SUCCESS_RESPONSE);

                saveReactivationAttachments(reactivationFile, hpProfile, requestId);
                log.info("ApplicationServiceImpl: reactivateRequest method: Execution Successful. ");
                return reactivateRequestResponseTo;
            } else {
                throw new WorkFlowException(NMRError.PROFILE_NOT_SUSPEND.getCode(), NMRError.PROFILE_NOT_SUSPEND.getMessage());
            }
        } else {
            throw new WorkFlowException(NMRError.REACTIVATION_REQUEST_ALREADY_EXISTS.getCode(), NMRError.REACTIVATION_REQUEST_ALREADY_EXISTS.getMessage());
        }
    }

    private void validateUserAccessToResourceForApplication(BigInteger hpProfileId) {
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
        } else if (UserTypeEnum.NMC.getId().equals(loggedInUser.getUserType().getId())) {
        } else {
            log.error("Access denied: You do not have permissions to access this resource.");
            throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
        }
    }

    private BigInteger getGroupIdForLoggedInUser() {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();
        log.info("Processing card-detail service for : {} ", userName);
        User userDetail = userDaoService.findByUsername(userName, userType);
        return userDetail.getGroup().getId();
    }

    private void saveReactivationAttachments(MultipartFile reactivationFile, HpProfile hpProfile, String requestId) throws IOException {
        UserAttachments userAttachment=new UserAttachments();
        userAttachment.setUserId(hpProfile.getUser().getId());
        userAttachment.setRequestId(requestId);
        userAttachment.setAttachmentTypeId(AttachmentType.REACTIVATION.getId());
        if (reactivationFile != null) {
            userAttachment.setName( reactivationFile.getOriginalFilename());
            if (minioEnable) {
                String fileName = hpProfile.getId() + "_" + reactivationFile.getOriginalFilename();
                String path = "NMR/HP/" + hpProfile.getUser().getId() + "/Reactivation/" + fileName;
                s3Service.uploadFile(path, reactivationFile);
                userAttachment.setFilePath(path);

            } else {
                userAttachment.setFileBytes(reactivationFile.getBytes());
            }
        }
        userAttachmentsRepository.save(userAttachment);
    }

    /**
     * Service Implementation's method for fetching the reactivation records of the health professionals
     * for the NMC to approve or reject their reactivation request.
     *
     * @param pageNo   - Gives the current page number
     * @param offset   - Gives the number of records to be displayed
     * @param sortBy   -  According to which column the sort has to happen
     * @param sortType -  Sorting order ASC or DESC
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    @Override
    public ReactivateHealthProfessionalResponseTO getReactivationRecordsOfHealthProfessionalsToNmc(String pageNo, String offset, String search, String value, String sortBy, String sortType) throws InvalidRequestException {
        ReactivateHealthProfessionalResponseTO reactivateHealthProfessionalResponseTO = null;
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();

        User userDetail = userDaoService.findByUsername(userName, userType);
        BigInteger groupId = userDetail.getGroup().getId();
        ReactivateHealthProfessionalRequestParam reactivateHealthProfessionalQueryParam = new ReactivateHealthProfessionalRequestParam();
        reactivateHealthProfessionalQueryParam.setGroupId(groupId.toString());
        reactivateHealthProfessionalQueryParam.setPageNo(Integer.parseInt(pageNo));
        final int dataLimit = Math.min(MAX_DATA_SIZE, Integer.parseInt(offset));
        reactivateHealthProfessionalQueryParam.setOffset(dataLimit);
        if (StringUtils.isNotBlank(search)) {
            if (value != null && !value.isBlank()) {
                switch (search.toLowerCase()) {
                    case APPLICANT_FULL_NAME_IN_LOWER_CASE:
                        reactivateHealthProfessionalQueryParam.setApplicantFullName(value);
                        break;
                    case REGISTRATION_NUMBER_IN_LOWER_CASE:
                        reactivateHealthProfessionalQueryParam.setRegistrationNumber(value);
                        break;
                    case GENDER_IN_LOWER_CASE:
                        reactivateHealthProfessionalQueryParam.setGender(value);
                        break;
                    case EMAIL_ID_IN_LOWER_CASE:
                        reactivateHealthProfessionalQueryParam.setEmailId(value);
                        break;
                    case MOBILE_NUMBER_IN_LOWER_CASE:
                        reactivateHealthProfessionalQueryParam.setMobileNumber(value);
                        break;
                    case YEAR_OF_REGISTRATION_IN_LOWER_CASE:
                        reactivateHealthProfessionalQueryParam.setYearOfRegistration(value);
                        break;
                    case SEARCH_IN_LOWER_CASE:
                        reactivateHealthProfessionalQueryParam.setSearch(value);
                        break;
                    default:
                        throw new InvalidRequestException(NMRError.INVALID_SEARCH_CRITERIA.getCode(), NMRError.INVALID_SEARCH_CRITERIA.getMessage());
                }
            } else {
                throw new InvalidRequestException(NMRError.MISSING_SEARCH_VALUE.getCode(), NMRError.MISSING_SEARCH_VALUE.getMessage());
            }
        }
        final String sortingOrder = sortType == null ? DEFAULT_SORT_ORDER : sortType;
        reactivateHealthProfessionalQueryParam.setSortType(sortingOrder);
        String column = getReactivationSortColumn(sortBy);
        reactivateHealthProfessionalQueryParam.setSortBy(column);
        if (StringUtils.isBlank(search) && StringUtils.isNotBlank(value)) {
            reactivateHealthProfessionalQueryParam.setApplicantFullName(value);
        }
        try {
            Pageable pageable = PageRequest.of(reactivateHealthProfessionalQueryParam.getPageNo(), reactivateHealthProfessionalQueryParam.getOffset());
            reactivateHealthProfessionalResponseTO = iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(reactivateHealthProfessionalQueryParam, pageable);
        } catch (Exception e) {
            log.error("Service exception " + e.getMessage());
        }
        return reactivateHealthProfessionalResponseTO;
    }

    private String getReactivationSortColumn(String columnToSort) {

        if (columnToSort != null && columnToSort.length() > 0) {
            return REACTIVATION_SORT_MAPPINGS.getOrDefault(columnToSort, " wf.created_at ");
        } else {
            return " wf.created_at ";
        }
    }


    /**
     * This method is used to initiate the workflow for a suspension and reactivate request.
     *
     * @param applicationRequestTo the request containing details of the action to be taken
     * @param requestId            the unique identifier for the request
     * @param newHpProfile         the new health professional profile created as a result of the request
     * @throws WorkFlowException if there is any error while initiating the workflow
     */
    private void initiateWorkFlow(ApplicationRequestTo applicationRequestTo, String requestId, HpProfile newHpProfile) throws WorkFlowException, InvalidRequestException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();

        User userDetail = userDaoService.findByUsername(userName, userType);
        WorkFlowRequestTO workFlowRequestTO = new WorkFlowRequestTO();
        workFlowRequestTO.setRequestId(requestId);
        workFlowRequestTO.setApplicationTypeId(applicationRequestTo.getApplicationTypeId());
        workFlowRequestTO.setActionId(applicationRequestTo.getActionId());
        workFlowRequestTO.setActorId(userDetail.getGroup().getId());
        workFlowRequestTO.setHpProfileId(newHpProfile.getId());
        workFlowRequestTO.setStartDate(applicationRequestTo.getFromDate());
        workFlowRequestTO.setEndDate(applicationRequestTo.getToDate());
        workFlowRequestTO.setRemarks(applicationRequestTo.getRemarks());
        workFlowRequestTO.setApplicationSubTypeId(applicationRequestTo.getApplicationSubTypeId());
        iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
    }

    /**
     * Retrieves information about the status of a health professional's requests for NMC, NBE, SMC, Dean, Registrar and Admin.
     *
     * @param nmrPagination the nmr pagination details object.
     * @param search the search label.
     * @param value the search value against a label.
     * @param smcId the smc id
     * @param registrationNo the registration number.
     * @return
     * @throws InvalidRequestException
     */
    @Override
    public HealthProfessionalApplicationResponseTo fetchApplicationDetails(NMRPagination nmrPagination, String search, String value, String smcId, String registrationNo) throws InvalidRequestException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();
        User userDetail = userDaoService.findByUsername(userName, userType);
        HealthProfessionalApplicationRequestParamsTo applicationRequestParamsTo = new HealthProfessionalApplicationRequestParamsTo();
        BigInteger groupId = userDetail.getGroup().getId();
        BigInteger userId = userDetail.getId();
        if(Group.SMC.getId().equals(groupId)){
            smcId =  iSmcProfileRepository.findByUserId(userId).getStateMedicalCouncil().getId().toString();
        }else if(Group.NBE.getId().equals(groupId)){
            applicationRequestParamsTo.setApplicationTypeId(ApplicationType.FOREIGN_HP_REGISTRATION.getId().toString());
        }else if(Group.COLLEGE.getId().equals(groupId)){
            applicationRequestParamsTo.setCollegeId(collegeProfileDaoService.findByUserId(userId).getCollege().getId().toString());
        }
        setHPRequestParamInToObject(applicationRequestParamsTo,nmrPagination, search, value, smcId, registrationNo, null);
        Pageable pageable = PageRequest.of(applicationRequestParamsTo.getPageNo(), applicationRequestParamsTo.getOffset());
        return iFetchTrackApplicationDetailsCustomRepository.fetchTrackApplicationDetails(applicationRequestParamsTo, pageable, Collections.emptyList());
    }

    private void setHPRequestParamInToObject(HealthProfessionalApplicationRequestParamsTo applicationRequestParamsTo, NMRPagination nmrPagination, String search, String value, String smcId, String registrationNo, BigInteger hpProfileId) throws InvalidRequestException {
        if (StringUtils.isNotBlank(search)) {
            if (value != null && !value.isBlank()) {
                switch (search.toLowerCase()) {
                    case WORK_FLOW_STATUS_ID_IN_LOWER_CASE:
                        applicationRequestParamsTo.setWorkFlowStatusId(value);
                        break;
                    case APPLICATION_TYPE_ID_IN_LOWER_CASE:
                        applicationRequestParamsTo.setApplicationTypeId(value);
                        break;
                    case REGISTRATION_NUMBER_IN_LOWER_CASE:
                        applicationRequestParamsTo.setRegistrationNumber(value);
                        break;
                    case SMC_ID_IN_LOWER_CASE:
                        applicationRequestParamsTo.setSmcId(value);
                        break;
                    case APPLICANT_FULL_NAME_IN_LOWER_CASE:
                        applicationRequestParamsTo.setApplicantFullName(value);
                        break;
                    case GENDER_IN_LOWER_CASE:
                        applicationRequestParamsTo.setGender(value);
                        break;
                    case EMAIL_ID_IN_LOWER_CASE:
                        applicationRequestParamsTo.setEmailId(value);
                        break;
                    case MOBILE_NUMBER_IN_LOWER_CASE:
                        applicationRequestParamsTo.setMobileNumber(value);
                        break;
                    case YEAR_OF_REGISTRATION_IN_LOWER_CASE:
                        applicationRequestParamsTo.setYearOfRegistration(value);
                        break;
                    case REQUEST_ID_IN_LOWER_CASE:
                        applicationRequestParamsTo.setRequestId(value);
                        break;
                    default:
                        throw new InvalidRequestException(NMRError.INVALID_SEARCH_CRITERIA.getCode(), NMRError.INVALID_SEARCH_CRITERIA.getMessage());
                }
            } else {
                throw new InvalidRequestException(NMRError.MISSING_SEARCH_VALUE.getCode(), NMRError.MISSING_SEARCH_VALUE.getMessage());
            }
        }
        if (Objects.nonNull(smcId)) {
            applicationRequestParamsTo.setSmcId(smcId);
        }
        if (Objects.nonNull(registrationNo)) {
            applicationRequestParamsTo.setRegistrationNumber(registrationNo);
        }
        if (StringUtils.isBlank(search) && StringUtils.isNotBlank(value)) {
            applicationRequestParamsTo.setApplicantFullName(value);
        }
        final int dataLimit = Math.min(MAX_DATA_SIZE, nmrPagination.getOffset());
        applicationRequestParamsTo.setOffset(dataLimit);
        applicationRequestParamsTo.setPageNo(nmrPagination.getPageNo());
        final String sortingOrder = (nmrPagination.getSortType() == null || nmrPagination.getSortType().trim().isEmpty()) ? DEFAULT_SORT_ORDER : nmrPagination.getSortType();
        String column = mapColumnToTable(nmrPagination.getSortBy());
        applicationRequestParamsTo.setSortBy(column);
        applicationRequestParamsTo.setSortOrder(sortingOrder);
        applicationRequestParamsTo.setHpProfileId(hpProfileId);

    }

    /**
     * Retrieves information about a health professional's application requests to track by health professional.
     *
     * @param healthProfessionalId the health professional id.
     * @param pageNo               - Gives the current page number
     * @param offset               - Gives the number of records to be displayed
     * @param sortBy               -  According to which column the sort has to happen
     * @param sortType             -  Sorting order ASC or DESC
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @Override
    public HealthProfessionalApplicationResponseTo fetchApplicationDetailsForHealthProfessional(BigInteger healthProfessionalId, String pageNo, String offset, String sortBy, String sortType, String search, String value) throws InvalidRequestException {
        iHpRegistrationService.validateUserAccessToResource(healthProfessionalId);
        RegistrationDetails registrationDetails = iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(healthProfessionalId);
        List<BigInteger> hpProfileIds = iRegistrationDetailRepository.fetchHpProfileIdByRegistrationNumber(registrationDetails.getRegistrationNo());
        HealthProfessionalApplicationRequestParamsTo applicationRequestParamsTo = new HealthProfessionalApplicationRequestParamsTo();
        setHPRequestParamInToObject(applicationRequestParamsTo,NMRPagination.builder().pageNo(Integer.valueOf(pageNo)).offset(Integer.valueOf(offset)).sortBy(sortBy).sortType(sortType).build(), search, value, null, null, healthProfessionalId);
        Pageable pageable = PageRequest.of(applicationRequestParamsTo.getPageNo(), applicationRequestParamsTo.getOffset());
        return iFetchTrackApplicationDetailsCustomRepository.fetchTrackApplicationDetails(applicationRequestParamsTo, pageable, hpProfileIds);
    }


    /**
     * This method fetches the application detail based on the given requestId and returns the ApplicationDetailResponseTo object.
     *
     * @param requestId - the id of the request for which application detail is required
     * @return ApplicationDetailResponseTo - the response object containing application detail
     */
    @Override
    public ApplicationDetailResponseTo fetchApplicationDetail(String requestId) throws InvalidRequestException {
        iHpRegistrationService.validateUserAccessToResource(iWorkFlowAuditRepository.fetchApplicationDetails(requestId).getHpProfile().getId());
        return iFetchTrackApplicationDetailsCustomRepository.fetchApplicationDetails(requestId);
    }

    /**
     * Maps the database column name to be used for sorting based on the columnToSort name.
     *
     * @param columnToSort - name of the column to be sorted
     * @return database column name to be used for sorting
     */
    private String mapColumnToTable(String columnToSort) {
        Map<String, String> columnToSortMap = new HashMap<>();
        columnToSortMap.put("doctorStatus", " doctor_status");
        columnToSortMap.put("councilVerificationStatus", " smc_status");
        columnToSortMap.put("collegeDeanStatus", " college_dean_status");
        columnToSortMap.put("collegeVerificationStatus", " nbe_status");
        columnToSortMap.put("NMCVerificationStatus", " nmc_status");
        columnToSortMap.put("nbeStatus", " nbe_status");
        columnToSortMap.put("hpProfileId", " d.hp_profile_id");
        columnToSortMap.put("requestId", " d.request_id");
        columnToSortMap.put("registrationNo", " rd.registration_no");
        columnToSortMap.put("dateofSubmission", " d.created_at");
        columnToSortMap.put("nameofStateCouncil", " stmc.name");
        columnToSortMap.put("nameofApplicant", " hp.full_name");
        columnToSortMap.put("applicationTypeId", " application_type_id");
        columnToSortMap.put("pendency", " pendency");
        return columnToSortMap.getOrDefault(columnToSort, " d.created_at ");
    }
}
