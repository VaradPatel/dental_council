package in.gov.abdm.nmr.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileAudit;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetailsAudit;
import in.gov.abdm.nmr.entity.WorkProfile;
import in.gov.abdm.nmr.entity.WorkProfileAudit;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.IHpProfileAuditMapper;
import in.gov.abdm.nmr.mapper.IHpProfileMapper;
import in.gov.abdm.nmr.repository.IHpProfileAuditRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IRegistrationDetailRepository;
import in.gov.abdm.nmr.repository.IWorkFlowAuditRepository;
import in.gov.abdm.nmr.repository.IWorkFlowRepository;
import in.gov.abdm.nmr.repository.RegistrationDetailAuditRepository;
import in.gov.abdm.nmr.repository.WorkProfileAuditRepository;
import in.gov.abdm.nmr.repository.WorkProfileRepository;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;

@Service
public class HpRegistrationServiceImpl implements IHpRegistrationService {
	@Autowired
	private IHpProfileMapper iHpProfileMapper;
	
	@Autowired
	private IHpProfileAuditMapper iHpProfileAuditMapper;

	@Autowired
	private IWorkFlowService iWorkFlowService;
	
	@Autowired
	private IRequestCounterService requestCounterService;
	
	@Autowired
	private IWorkFlowRepository workFlowRepository;
	
	@Autowired
	private IWorkFlowAuditRepository iWorkFlowAuditRepository;
	
	@Autowired
	private IHpProfileAuditRepository iHpProfileAuditRepository;
	
	@Autowired
	private IHpProfileRepository iHpProfileRepository;
	
	@Autowired
	private IRegistrationDetailRepository registrationDetailRepository;
	
	@Autowired
	private RegistrationDetailAuditRepository registrationDetailAuditRepository;
	
	@Autowired
	private WorkProfileRepository workProfileRepository;
	
	@Autowired
	private WorkProfileAuditRepository workProfileAuditRepository;

	@Autowired
	private IHpProfileDaoService hpProfileDaoService;



	@Override
	public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(
			SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
		return iHpProfileMapper
				.SmcRegistrationToDto(hpProfileDaoService.fetchSmcRegistrationDetail(smcRegistrationDetailRequestTO));
	}

	private void addHpProfileInHpProfileAudit(BigInteger hpProfileId) {
		HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);
		HpProfileAudit hpProfileAudit = iHpProfileAuditMapper.hpProfileToHpProfileAudit(hpProfile);
		iHpProfileAuditRepository.save(hpProfileAudit);
	}
	
	private void addRegistrationDetailsInRegistrationAudit(BigInteger hpProfileId) {
		RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
		RegistrationDetailsAudit registrationDetailsAudit = iHpProfileAuditMapper.registrationDetailsToRegistrationDetailsAudit(registrationDetails);
		registrationDetailAuditRepository.save(registrationDetailsAudit);
	}

	private void addWorkProfileToWorkProfileAudit(BigInteger hpProfileId) {
		WorkProfile workProfile = workProfileRepository.getWorkProfileByHpProfileId(hpProfileId);
		WorkProfileAudit workProfileAudit = iHpProfileAuditMapper.workProfileToWorkProfileAudit(workProfile);
		workProfileAuditRepository.save(workProfileAudit);
	}
	
	
	@Override
	public HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException {
		return iHpProfileMapper.HpProfilePictureUploadToDto(hpProfileDaoService.uploadHpProfilePhoto(file, hpProfileId));
	}

	@Override
	public String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs) throws WorkFlowException {
		for(QualificationDetailRequestTO qualificationDetailRequestTO: qualificationDetailRequestTOs){
			String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.QUALIFICATION_ADDITION.getId()));
			WorkFlowRequestTO workFlowRequestTO = new WorkFlowRequestTO();
			workFlowRequestTO.setRequestId(requestId);
			workFlowRequestTO.setActionId(Action.SUBMIT.getId());
			workFlowRequestTO.setActorId(Group.HEALTH_PROFESSIONAL.getId());
			workFlowRequestTO.setApplicationTypeId(ApplicationType.QUALIFICATION_ADDITION.getId());
			workFlowRequestTO.setHpProfileId(hpProfileId);
			qualificationDetailRequestTO.setRequestId(requestId);
			iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
		}
		hpProfileDaoService.saveQualificationDetails(hpProfileDaoService.findById(hpProfileId), null, qualificationDetailRequestTOs);
		return "Success";
	}

	
	@Override
	public HpProfileUpdateResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
																 HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException {
		return hpProfileDaoService.updateHpPersonalDetails(hpProfileId, hpPersonalUpdateRequestTO);
	}

	@Override
	public HpProfileUpdateResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
																	 HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO) {
		return iHpProfileMapper
				.HpProfileUpdateToDto(hpProfileDaoService.updateHpRegistrationDetails(hpProfileId, hpRegistrationUpdateRequestTO));
	}

	@Override
	public HpProfileUpdateResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
																  HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO) {
		return iHpProfileMapper
				.HpProfileUpdateToDto(hpProfileDaoService.updateWorkProfileDetails(hpProfileId, hpWorkProfileUpdateRequestTO));
	}

	@Override
	public HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO)
			throws InvalidRequestException, WorkFlowException {
		if(hpSubmitRequestTO.getHpProfileId() != null &&
				iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpSubmitRequestTO.getHpProfileId())){
			throw new WorkFlowException("Cant create new request until an existing request is closed.", HttpStatus.BAD_REQUEST);
		}
		if (hpSubmitRequestTO.getRequestId() != null && WorkflowStatus.QUERY_RAISED.getId().equals(workFlowRepository.findByRequestId(hpSubmitRequestTO.getRequestId()).getWorkFlowStatus().getId())) {
			iWorkFlowService.assignQueriesBackToQueryCreator(hpSubmitRequestTO.getRequestId());
		}else {
			String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(hpSubmitRequestTO.getApplicationTypeId()));
			WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
					.applicationTypeId(hpSubmitRequestTO.getApplicationTypeId())
					.hpProfileId(hpSubmitRequestTO.getHpProfileId())
					.actionId(Action.SUBMIT.getId())
					.actorId(Group.HEALTH_PROFESSIONAL.getId())
					.build();
			iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
			HpProfile hpProfileById = iHpProfileRepository.findHpProfileById(hpSubmitRequestTO.getHpProfileId());
			hpProfileById.setRequestId(requestId);
		}
		return new HpProfileAddResponseTO(201, "Hp Profile Submitted Successfully!", hpSubmitRequestTO.getHpProfileId());
	}

}
