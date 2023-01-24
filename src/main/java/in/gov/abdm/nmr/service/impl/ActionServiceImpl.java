package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.ActionRequestTo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionServiceImpl implements IActionService {

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
    ICustomQualificationDetailRepository iCustomQualificationDetailRepository;
    @Autowired
    SuperSpecialityRepository superSpecialityRepository;
    @Autowired
    private IRequestCounterService requestCounterService;
    @Autowired
    private IWorkFlowService iWorkFlowService;

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

        List<CustomQualificationDetails> customQualificationDetailsList = new ArrayList<>();
        List<CustomQualificationDetails> customQualificationDetails = iCustomQualificationDetailRepository.getQualificationDetailsByHpProfileId(existingHpProfile.getId());
        for (CustomQualificationDetails customQualificationDetail : customQualificationDetails) {
            CustomQualificationDetails newCustomQualificationDetails = new CustomQualificationDetails();
            org.springframework.beans.BeanUtils.copyProperties(customQualificationDetail, newCustomQualificationDetails);
            newCustomQualificationDetails.setId(null);
            newCustomQualificationDetails.setHpProfile(targetedHpProfile);
            customQualificationDetailsList.add(newCustomQualificationDetails);
        }
        iCustomQualificationDetailRepository.saveAll(customQualificationDetailsList);

        List<SuperSpeciality> superSpecialities = new ArrayList<>();
        List<SuperSpeciality> superSpecialityList = superSpecialityRepository.getSuperSpecialityFromHpProfileId(existingHpProfile.getId());
        for(SuperSpeciality superSpeciality : superSpecialityList){
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
