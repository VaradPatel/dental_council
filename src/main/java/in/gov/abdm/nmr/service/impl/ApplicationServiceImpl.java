package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IApplicationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.gov.abdm.nmr.util.NMRConstants.DEFAULT_SORT_ORDER;
import static in.gov.abdm.nmr.util.NMRConstants.MAX_DATA_SIZE;

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
     * Injecting a SuperSpecialityRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private SuperSpecialityRepository superSpecialityRepository;

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
    private IUserRepository userDetailRepository;

    /**
     * Injecting a IHpProfileRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IHpProfileRepository hpProfileRepository;


    private static final Map<String, String> REACTIVATION_SORT_MAPPINGS = Map.of("id", " r.id", "name", " r.full_name", "createdAt", " r.created_at", "reactivationDate", " r.start_date", "suspensionType", " r.suspension_type", "remarks", " r.remarks");

    /**
     * This method is used to suspend a health professional based on the request provided.
     *
     * @param applicationRequestTo the request object containing necessary information to suspend a health professional.
     * @return a string indicating the result of the suspension request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    @Override
    public String suspendRequest(ApplicationRequestTo applicationRequestTo) throws WorkFlowException {
        String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(applicationRequestTo.getApplicationTypeId()));
        HpProfile newHpProfile = createNewHpProfile(applicationRequestTo, requestId);
        initiateWorkFlow(applicationRequestTo, requestId, newHpProfile);
        return newHpProfile.getId().toString();
    }

    /**
     * This method is used to reactivate a health professional based on the request provided.
     *
     * @param applicationRequestTo the request object containing necessary information to reactivate a health professional.
     * @return a string indicating the result of the reactivate request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    @Override
    public String reactiveRequest(ApplicationRequestTo applicationRequestTo) throws WorkFlowException, NmrException {
        HpProfile hpProfile = hpProfileRepository.findHpProfileById(applicationRequestTo.getHpProfileId());
        if ((HpProfileStatus.SUSPENDED.getId()) == hpProfile.getHpProfileStatus().getId()) {
            String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(applicationRequestTo.getApplicationTypeId()));
            HpProfile newHpProfile = createNewHpProfile(applicationRequestTo, requestId);
            initiateWorkFlow(applicationRequestTo, requestId, newHpProfile);
            return newHpProfile.getId().toString();
        } else {
            throw new NmrException("Suspended profile can only be reactivated", HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Service Implementation's method for fetching the reactivation records of the health professionals
     * for the NMC to approve or reject their reactivation request.
     *
     * @param pageNo   - Gives the current page number
     * @param offset   - Gives the number of records to be displayed
     * @param search   - Gives the search criteria like HP_Id, HP_name, Submiited_Date, Remarks
     * @param sortBy   -  According to which column the sort has to happen
     * @param sortType -  Sorting order ASC or DESC
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    @Override
    public ReactivateHealthProfessionalResponseTO getReactivationRecordsOfHealthProfessionalsToNmc(String pageNo, String offset, String search, String sortBy, String sortType) {
        ReactivateHealthProfessionalResponseTO reactivateHealthProfessionalResponseTO = null;
        ReactivateHealthProfessionalRequestParam reactivateHealthProfessionalQueryParam = new ReactivateHealthProfessionalRequestParam();
        reactivateHealthProfessionalQueryParam.setPageNo(Integer.parseInt(pageNo));
        final int dataLimit = Math.min(MAX_DATA_SIZE, Integer.parseInt(offset));
        reactivateHealthProfessionalQueryParam.setOffset(dataLimit);
        reactivateHealthProfessionalQueryParam.setSearch(search);
        final String sortingOrder = sortType == null ? DEFAULT_SORT_ORDER : sortType;
        reactivateHealthProfessionalQueryParam.setSortType(sortingOrder);
        String column = getReactivationSortColumn(sortBy);
        reactivateHealthProfessionalQueryParam.setSortBy(column);
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
            return REACTIVATION_SORT_MAPPINGS.getOrDefault(columnToSort, " r.created_at ");
        } else {
            return " r.created_at ";
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
    private void initiateWorkFlow(ApplicationRequestTo applicationRequestTo, String requestId, HpProfile newHpProfile) throws WorkFlowException {
        WorkFlowRequestTO workFlowRequestTO = new WorkFlowRequestTO();
        workFlowRequestTO.setRequestId(requestId);
        workFlowRequestTO.setApplicationTypeId(applicationRequestTo.getApplicationTypeId());
        workFlowRequestTO.setActionId(Action.SUBMIT.getId());
        workFlowRequestTO.setActorId(Group.HEALTH_PROFESSIONAL.getId());
        workFlowRequestTO.setApplicationTypeId(applicationRequestTo.getApplicationTypeId());
        workFlowRequestTO.setActionId(applicationRequestTo.getActionId());
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetail = userDetailRepository.findByUsername(userName);
        workFlowRequestTO.setActorId(userDetail.getGroup().getId());
        workFlowRequestTO.setHpProfileId(newHpProfile.getId());
        workFlowRequestTO.setStartDate(applicationRequestTo.getFromDate());
        workFlowRequestTO.setEndDate(applicationRequestTo.getToDate());
        workFlowRequestTO.setRemarks(applicationRequestTo.getRemarks());
        iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
    }

    private HpProfile createNewHpProfile(ApplicationRequestTo applicationRequestTo, String requestId) {
        HpProfile existingHpProfile = iHpProfileRepository.findHpProfileById(applicationRequestTo.getHpProfileId());
        HpProfile targetedHpProfile = new HpProfile();
        org.springframework.beans.BeanUtils.copyProperties(existingHpProfile, targetedHpProfile);
        targetedHpProfile.setId(null);
        targetedHpProfile.setRequestId(requestId);
        iHpProfileRepository.save(targetedHpProfile);

        RegistrationDetails registrationDetails = iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(existingHpProfile.getId());
        RegistrationDetails newRegistrationDetails = new RegistrationDetails();
        org.springframework.beans.BeanUtils.copyProperties(registrationDetails, newRegistrationDetails);
        newRegistrationDetails.setId(null);
        newRegistrationDetails.setHpProfileId(targetedHpProfile);
        iRegistrationDetailRepository.save(newRegistrationDetails);

        List<WorkProfile> workProfileList = new ArrayList<>();
        List<WorkProfile> workProfiles = workProfileRepository.getWorkProfileDetailsByHPId(existingHpProfile.getId());
        workProfiles.forEach(workProfile -> {
            WorkProfile newWorkProfile = new WorkProfile();
            org.springframework.beans.BeanUtils.copyProperties(workProfile, newWorkProfile);
            newWorkProfile.setId(null);
            newWorkProfile.setHpProfileId(targetedHpProfile.getId());
            workProfileList.add(newWorkProfile);
        });
        workProfileRepository.saveAll(workProfileList);

        List<LanguagesKnown> languagesKnownList = new ArrayList<>();
        List<LanguagesKnown> languagesKnown = languagesKnownRepository.getLanguagesKnownByHpProfileId(existingHpProfile.getId());
        for (LanguagesKnown languageKnown : languagesKnown) {
            LanguagesKnown newLanguagesKnown = new LanguagesKnown();
            org.springframework.beans.BeanUtils.copyProperties(languageKnown, newLanguagesKnown);
            newLanguagesKnown.setId(null);
            newLanguagesKnown.setHpProfile(targetedHpProfile);
            languagesKnownList.add(newLanguagesKnown);
        }
        languagesKnownRepository.saveAll(languagesKnownList);

        List<QualificationDetails> qualificationDetails = new ArrayList<>();
        List<QualificationDetails> qualificationDetailsList = iQualificationDetailRepository.getQualificationDetailsByHpProfileId(existingHpProfile.getId());
        for (QualificationDetails qualificationDetail : qualificationDetailsList) {
            QualificationDetails newQualificationDetails = new QualificationDetails();
            org.springframework.beans.BeanUtils.copyProperties(qualificationDetail, newQualificationDetails);
            newQualificationDetails.setId(null);
            newQualificationDetails.setHpProfile(targetedHpProfile);
            qualificationDetails.add(newQualificationDetails);
        }
        iQualificationDetailRepository.saveAll(qualificationDetails);

        List<ForeignQualificationDetails> customQualificationDetailsList = new ArrayList<>();
        List<ForeignQualificationDetails> customQualificationDetails = iForeignQualificationDetailRepository.getQualificationDetailsByHpProfileId(existingHpProfile.getId());
        for (ForeignQualificationDetails customQualificationDetail : customQualificationDetails) {
            ForeignQualificationDetails newCustomQualificationDetails = new ForeignQualificationDetails();
            org.springframework.beans.BeanUtils.copyProperties(customQualificationDetail, newCustomQualificationDetails);
            newCustomQualificationDetails.setId(null);
            newCustomQualificationDetails.setHpProfile(targetedHpProfile);
            customQualificationDetailsList.add(newCustomQualificationDetails);
        }
        iForeignQualificationDetailRepository.saveAll(customQualificationDetailsList);

        List<SuperSpeciality> superSpecialities = new ArrayList<>();
        List<SuperSpeciality> superSpecialityList = superSpecialityRepository.getSuperSpecialityFromHpProfileId(existingHpProfile.getId());
        for (SuperSpeciality superSpeciality : superSpecialityList) {
            SuperSpeciality newSuperSpeciality = new SuperSpeciality();
            org.springframework.beans.BeanUtils.copyProperties(superSpeciality, newSuperSpeciality);
            newSuperSpeciality.setId(null);
            newSuperSpeciality.setHpProfileId(targetedHpProfile.getId());
            superSpecialities.add(newSuperSpeciality);
        }
        superSpecialityRepository.saveAll(superSpecialities);
        return targetedHpProfile;
    }

    /**
     * Retrieves information about the status of a health professional's requests for NMC, NBE, SMC, Dean, Registrar and Admin.
     *
     * @param pageNo            - Gives the current page number
     * @param offset            - Gives the number of records to be displayed
     * @param workFlowStatusId  - Search by work flow status Id
     * @param applicationTypeId - Search by application type Id
     * @param smcId             - Search by SMC Id
     * @param registrationNo    - Search by registrationNo
     * @param sortBy            -  According to which column the sort has to happen
     * @param sortType          -  Sorting order ASC or DESC
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @Override
    public HealthProfessionalApplicationResponseTo fetchApplicationDetails(String pageNo, String offset, String sortBy, String sortType, String workFlowStatusId, String applicationTypeId, String smcId, String registrationNo) {
        HealthProfessionalApplicationRequestParamsTo applicationRequestParamsTo = setHPRequestParamInToObject(pageNo, offset, sortBy, sortType, workFlowStatusId, applicationTypeId, smcId, registrationNo, null);
        Pageable pageable = PageRequest.of(applicationRequestParamsTo.getPageNo(), applicationRequestParamsTo.getSize());
        return iFetchTrackApplicationDetailsCustomRepository.fetchTrackApplicationDetails(applicationRequestParamsTo, pageable);
    }

    private HealthProfessionalApplicationRequestParamsTo setHPRequestParamInToObject(String pageNo, String offset, String sortBy, String sortType, String workFlowStatusId, String applicationTypeId, String smcId, String registrationNo, BigInteger hpProfileId) {
        HealthProfessionalApplicationRequestParamsTo applicationRequestParamsTo = new HealthProfessionalApplicationRequestParamsTo();
        applicationRequestParamsTo.setSmcId(smcId);
        applicationRequestParamsTo.setRegistrationNo(registrationNo);
        applicationRequestParamsTo.setWorkFlowStatusId(workFlowStatusId);
        applicationRequestParamsTo.setApplicationTypeId(applicationTypeId);
        String column = getColumnToSort(sortBy);
        final int dataLimit = Math.min(MAX_DATA_SIZE, Integer.parseInt(offset));
        applicationRequestParamsTo.setSize(dataLimit);
        applicationRequestParamsTo.setPageNo(Integer.parseInt(pageNo));
        final String sortingOrder = sortType == null ? DEFAULT_SORT_ORDER : sortType;
        applicationRequestParamsTo.setSortBy(column);
        applicationRequestParamsTo.setSortOrder(sortingOrder);
        applicationRequestParamsTo.setHpProfileId(hpProfileId);
        return applicationRequestParamsTo;
    }

    /**
     * Retrieves information about a health professional's application requests to track by health professional.
     *
     * @param healthProfessionalId the health professional id.
     * @param pageNo               - Gives the current page number
     * @param offset               - Gives the number of records to be displayed
     * @param workFlowStatusId     - Search by work flow status Id
     * @param applicationTypeId    - Search by application type Id
     * @param smcId                - Search by SMC Id
     * @param registrationNo       - Search by registrationNo
     * @param sortBy               -  According to which column the sort has to happen
     * @param sortType             -  Sorting order ASC or DESC
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @Override
    public HealthProfessionalApplicationResponseTo fetchApplicationDetailsForHealthProfessional(BigInteger healthProfessionalId, String pageNo, String offset, String sortBy, String sortType, String workFlowStatusId, String applicationTypeId, String smcId, String registrationNo) {
        RegistrationDetails registrationDetails = iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(healthProfessionalId);
        List<HpProfile> hpProfiles = iHpProfileRepository.findByRegistrationId(new BigInteger(registrationDetails.getRegistrationNo()));
        HealthProfessionalApplicationRequestParamsTo applicationRequestParamsTo = setHPRequestParamInToObject(pageNo, offset, sortBy, sortType, workFlowStatusId, applicationTypeId, smcId, registrationNo, healthProfessionalId);
        Pageable pageable = PageRequest.of(applicationRequestParamsTo.getPageNo(), applicationRequestParamsTo.getSize());
        return iFetchTrackApplicationDetailsCustomRepository.fetchTrackApplicationDetails(applicationRequestParamsTo, pageable);
    }

    /**
     * Maps the database column name to be used for sorting based on the columnToSort name.
     *
     * @param columnToSort - name of the column to be sorted
     * @return database column name to be used for sorting
     */
    private String getColumnToSort(String columnToSort) {
        Map<String, String> columns;
        if (columnToSort != null && columnToSort.length() > 0) {
            columns = mapColumnToTable();
            return columns.getOrDefault(columnToSort, " rd.created_at ");
        } else {
            return " rd.created_at ";
        }
    }

    private Map<String, String> mapColumnToTable() {
        Map<String, String> columnToSortMap = new HashMap<>();
        columnToSortMap.put("doctorStatus", " doctor_status");
        columnToSortMap.put("smcStatus", " smc_status");
        columnToSortMap.put("collegeDeanStatus", " college_dean_status");
        columnToSortMap.put("collegeRegistrarStatus", " college_registrar_status");
        columnToSortMap.put("nmcStatus", " nmc_status");
        columnToSortMap.put("nbeStatus", " nbe_status");
        columnToSortMap.put("hpProfileId", " calculate.hp_profile_id");
        columnToSortMap.put("requestId", " calculate.request_id");
        columnToSortMap.put("registrationNo", " rd.registration_no");
        columnToSortMap.put("createdAt", " rd.created_at");
        columnToSortMap.put("councilName", " stmc.name");
        columnToSortMap.put("applicantFullName", " hp.full_name");
        columnToSortMap.put("applicationTypeId", " application_type_id");
        return columnToSortMap;
    }

}
