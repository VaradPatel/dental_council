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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A class that implements all the methods of the interface  IActionService
 * which deals with the suspension and reactivation requests
 * */
@Service
@Slf4j
public class ActionServiceImpl implements IActionService {
    @Value("${max.data.size}")
    private Integer maxSize;
    @Value("${sort.order}")
    private String defaultSortOrder;
    @Autowired
    IHpProfileRepository iHpProfileRepository;
    @Autowired
    IRegistrationDetailRepository iRegistrationDetailRepository;
    @Autowired
    WorkProfileRepository workProfileRepository;
    @Autowired
    LanguagesKnownRepository languagesKnownRepository;
    @Autowired
    IQualificationDetailRepository iQualificationDetailRepository;
    @Autowired
    IForeignQualificationDetailRepository iForeignQualificationDetailRepository;
    @Autowired
    SuperSpecialityRepository superSpecialityRepository;
    @Autowired
    private IRequestCounterService requestCounterService;
    @Autowired
    private IWorkFlowService iWorkFlowService;
    @Autowired
    private IWorkFlowCustomRepository iWorkFlowCustomRepository;

    @Override
    public String suspendRequest(ActionRequestTo actionRequestTo) throws WorkFlowException {
        String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(actionRequestTo.getApplicationTypeId()));
        HpProfile newHpProfile = createNewHpProfile(actionRequestTo, requestId);
        initiateWorkFlow(actionRequestTo, requestId, newHpProfile);
        return newHpProfile.getId().toString();
    }

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
        final Integer dataLimit = maxSize < Integer.valueOf(offset) ? maxSize : Integer.valueOf(offset);
        reactivateHealthProfessionalQueryParam.setOffset(dataLimit);
        reactivateHealthProfessionalQueryParam.setSearch(search);
        final String sortingOrder = sortType == null ? defaultSortOrder : sortType;
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
