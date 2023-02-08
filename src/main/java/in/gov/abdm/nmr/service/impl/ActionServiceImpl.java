package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.ActionRequestTo;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalRequestParam;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IActionService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.gov.abdm.nmr.util.NMRConstants.DEFAULT_SORT_ORDER;
import static in.gov.abdm.nmr.util.NMRConstants.MAX_DATA_SIZE;

/**
 * A class that implements all the methods of the interface  IActionService
 * which deals with the suspension and reactivation requests
 * */
@Service
@Slf4j
public class ActionServiceImpl implements IActionService {

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
     * This method is used to suspend a health professional based on the request provided.
     * @param actionRequestTo the request object containing necessary information to suspend a health professional.
     * @return a string indicating the result of the suspension request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    @Override
    public String suspendRequest(ActionRequestTo actionRequestTo) throws WorkFlowException {
        String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(actionRequestTo.getApplicationTypeId()));
        HpProfile newHpProfile = createNewHpProfile(actionRequestTo, requestId);
        initiateWorkFlow(actionRequestTo, requestId, newHpProfile);
        return newHpProfile.getId().toString();
    }

    /**
     * This method is used to reactivate a health professional based on the request provided.
     * @param actionRequestTo the request object containing necessary information to reactivate a health professional.
     * @return a string indicating the result of the reactivate request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    @Override
    public String reactiveRequest(ActionRequestTo actionRequestTo) throws WorkFlowException {
        String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(actionRequestTo.getApplicationTypeId()));
        HpProfile newHpProfile = createNewHpProfile(actionRequestTo, requestId);
        initiateWorkFlow(actionRequestTo, requestId, newHpProfile);
        return newHpProfile.getId().toString();
    }

    /**
     * Service Implementation's method for fetching the reactivation records of the health professionals
     * for the NMC to approve or reject their reactivation request.
     *
     * @param pageNo       - Gives the current page number
     * @param offset        - Gives the number of records to be displayed
     * @param search       - Gives the search criteria like HP_Id, HP_name, Submiited_Date, Remarks
     * @param sortBy -  According to which column the sort has to happen
     * @param sortType    -  Sorting order ASC or DESC
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    @Override
    public ReactivateHealthProfessionalResponseTO getReactivationRecordsOfHealthProfessionalsToNmc(String pageNo, String offset, String search, String sortBy, String sortType) {
        ReactivateHealthProfessionalResponseTO reactivateHealthProfessionalResponseTO = null;
        ReactivateHealthProfessionalRequestParam reactivateHealthProfessionalQueryParam = new ReactivateHealthProfessionalRequestParam();
        reactivateHealthProfessionalQueryParam.setPageNo(Integer.valueOf(pageNo));
        final Integer dataLimit = MAX_DATA_SIZE < Integer.valueOf(offset) ? MAX_DATA_SIZE : Integer.valueOf(offset);
        reactivateHealthProfessionalQueryParam.setOffset(dataLimit);
        reactivateHealthProfessionalQueryParam.setSearch(search);
        final String sortingOrder = sortType == null ? DEFAULT_SORT_ORDER : sortType;
        reactivateHealthProfessionalQueryParam.setSortType(sortingOrder);
        String column = getColumnToSort(sortBy);
        reactivateHealthProfessionalQueryParam.setSortBy(column);
        try {
            Pageable pageable = PageRequest.of(reactivateHealthProfessionalQueryParam.getPageNo(), reactivateHealthProfessionalQueryParam.getOffset());
            reactivateHealthProfessionalResponseTO = iWorkFlowCustomRepository.getReactivationRecordsOfHealthProfessionalsToNmc(reactivateHealthProfessionalQueryParam, pageable);
        } catch (Exception e) {
            log.error("Service exception " + e.getMessage());
        }
        return reactivateHealthProfessionalResponseTO;
    }

    private String getColumnToSort(String columnToSort) {
        Map<String, String> columns;
        if (columnToSort != null && columnToSort.length() > 0) {
            columns = mapColumnToTable();
            if (columns.containsKey(columnToSort)) {
                return columns.get(columnToSort);
            } else {
                return " wf.created_at ";
            }
        } else {
            return " wf.created_at ";
        }
    }

    private Map<String, String> mapColumnToTable() {
        Map<String, String> columnToSortMap = new HashMap<>();
        columnToSortMap.put("id", " hp.id");
        columnToSortMap.put("name", " hp.full_name");
        columnToSortMap.put("createdAt", " wf.created_at");
        columnToSortMap.put("reactivationDate", " wf.start_date");
        columnToSortMap.put("suspensionType", " a.name");
        columnToSortMap.put("remarks", " wf.remarks");
        return columnToSortMap;
    }

    /**
     * This method is used to initiate the workflow for a suspension and reactivate request.
     * @param actionRequestTo the request containing details of the action to be taken
     * @param requestId the unique identifier for the request
     * @param newHpProfile the new health professional profile created as a result of the request
     * @throws WorkFlowException if there is any error while initiating the workflow
     */
    private void initiateWorkFlow(ActionRequestTo actionRequestTo, String requestId, HpProfile newHpProfile) throws WorkFlowException {
        WorkFlowRequestTO workFlowRequestTO = new WorkFlowRequestTO();
        workFlowRequestTO.setRequestId(requestId);
        workFlowRequestTO.setApplicationTypeId(actionRequestTo.getApplicationTypeId());
        workFlowRequestTO.setActionId(Action.SUBMIT.getId());
        workFlowRequestTO.setActorId(Group.HEALTH_PROFESSIONAL.getId());
        workFlowRequestTO.setHpProfileId(newHpProfile.getId());
        workFlowRequestTO.setStartDate(actionRequestTo.getFromDate());
        workFlowRequestTO.setEndDate(actionRequestTo.getToDate());
        workFlowRequestTO.setRemarks(actionRequestTo.getRemarks());
        iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
    }

    private HpProfile createNewHpProfile(ActionRequestTo actionRequestTo, String requestId) {
        HpProfile existingHpProfile = iHpProfileRepository.findHpProfileById(actionRequestTo.getHpProfileId());
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

        WorkProfile workProfile = workProfileRepository.getWorkProfileByHpProfileId(existingHpProfile.getId());
        WorkProfile newWorkProfile = new WorkProfile();
        org.springframework.beans.BeanUtils.copyProperties(workProfile, newWorkProfile);
        newWorkProfile.setId(null);
        newWorkProfile.setHpProfileId(targetedHpProfile.getId());
        workProfileRepository.save(newWorkProfile);

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
}
