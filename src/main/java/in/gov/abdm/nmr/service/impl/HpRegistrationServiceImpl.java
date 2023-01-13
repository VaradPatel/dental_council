package in.gov.abdm.nmr.service.impl;

import java.io.IOException;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.dto.HpProfileAddResponseTO;
import in.gov.abdm.nmr.dto.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateRequestTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileAudit;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetailsAudit;
import in.gov.abdm.nmr.entity.WorkProfile;
import in.gov.abdm.nmr.entity.WorkProfileAudit;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.WorkflowStatus;
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

	private HpProfileDaoServiceImpl hpProfileService;

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

	public HpRegistrationServiceImpl(HpProfileDaoServiceImpl hpProfileService, IHpProfileMapper iHpProfileMapper) {
		super();
		this.hpProfileService = hpProfileService;
		this.iHpProfileMapper = iHpProfileMapper;
	}

	@Override
	public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(
			SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
		return iHpProfileMapper
				.SmcRegistrationToDto(hpProfileService.fetchSmcRegistrationDetail(smcRegistrationDetailRequestTO));
	}

	@Override
	public HpProfileDetailResponseTO fetchHpProfileDetail(BigInteger hpProfileId) {
		return iHpProfileMapper.HpProfileDetailToDto(hpProfileService.fetchHpProfileDetail(hpProfileId));
	}

	@Override
	public HpProfileUpdateResponseTO updateHpProfileDetail(BigInteger hpProfileId,
			HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException, WorkFlowException {
		if(iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpProfileId)){
			throw new WorkFlowException("Cant create new request until an existing request is closed.", HttpStatus.BAD_REQUEST);
		}
		
		
		if (workFlowRepository.findByRequestId(hpProfileUpdateRequest.getRequestId()).getWorkFlowStatus().getId().equals(WorkflowStatus.QUERY_RAISED.getId())) {
			
			HpProfileUpdateResponseTO hpProfileUpdateResponseTO = iHpProfileMapper
					.HpProfileUpdateToDto(hpProfileService.updateHpProfile(hpProfileId, hpProfileUpdateRequest));
			
			iWorkFlowService.assignQueriesBackToQueryCreator(hpProfileUpdateRequest.getRequestId());
			return hpProfileUpdateResponseTO;
		}
		if(iWorkFlowService.isAnyApprovedWorkflowForHealthProfessional(hpProfileId)) { 
			String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.HP_MODIFICATION.getId()));

			hpProfileUpdateRequest.setRequestId(requestId);
			
			addHpProfileInHpProfileAudit(hpProfileId);
			
			addRegistrationDetailsInRegistrationAudit(hpProfileId);
			
			addWorkProfileToWorkProfileAudit(hpProfileId);
			
			HpProfileUpdateResponseTO hpProfileUpdateResponseTO = iHpProfileMapper
					.HpProfileUpdateToDto(hpProfileService.updateHpProfile(hpProfileId, hpProfileUpdateRequest));
			
			
			WorkFlowRequestTO approvedWorkFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
					.applicationTypeId(ApplicationType.HP_MODIFICATION.getId())
					.hpProfileId(hpProfileId)
					.actionId(Action.SUBMIT.getId())
					.actorId(Group.HEALTH_PROFESSIONAL.getId())
					.build();
			iWorkFlowService.initiateSubmissionWorkFlow(approvedWorkFlowRequestTO);		
			
			return hpProfileUpdateResponseTO;
		}
		return null;
	}
	
	private void addHpProfileInHpProfileAudit(BigInteger hpProfileId) {
		HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);
		
		HpProfileAudit hpProfileAudit = iHpProfileAuditMapper.HpProfileDetailAuditToDto(hpProfile);
				
		iHpProfileAuditRepository.save(hpProfileAudit);
	}
	
	private void addRegistrationDetailsInRegistrationAudit(BigInteger hpProfileId) {
		RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
				
		RegistrationDetailsAudit registrationDetailsAudit = iHpProfileAuditMapper.RegistrationDetailsAuditToDto(registrationDetails);
		
		registrationDetailAuditRepository.save(registrationDetailsAudit);
	}

	private void addWorkProfileToWorkProfileAudit(BigInteger hpProfileId) {
		WorkProfile workProfile = workProfileRepository.getWorkProfileByHpProfileId(hpProfileId);
		
		WorkProfileAudit workProfileAudit = iHpProfileAuditMapper.workProfileAuditToDto(workProfile);
		
		workProfileAuditRepository.save(workProfileAudit);
	}
	
	
	@Override
	public HpProfileAddResponseTO addHpProfileDetail(HpProfileAddRequestTO hpProfileAddRequestTO)
			throws InvalidRequestException, WorkFlowException {
		if(hpProfileAddRequestTO.getRegistrationDetail().getHpProfileId() != null &&
				iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpProfileAddRequestTO.getRegistrationDetail().getHpProfileId())){
			throw new WorkFlowException("Cant create new request until an existing request is closed.", HttpStatus.BAD_REQUEST);
		}
		String requestId = hpProfileAddRequestTO.getRequestId();
		
		if(hpProfileAddRequestTO.getRequestId() == null){
			requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.HP_REGISTRATION.getId()));
		}
		
		HpProfileAddResponseTO hpProfileAddResponseTO = hpProfileService.addHpProfile(hpProfileAddRequestTO, requestId);

		WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
				.applicationTypeId(ApplicationType.HP_REGISTRATION.getId())
				.hpProfileId(hpProfileAddResponseTO.getHpProfileId())
				.actionId(Action.SUBMIT.getId())
				.actorId(Group.HEALTH_PROFESSIONAL.getId())
				.build();
		
		iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
		
		return iHpProfileMapper.HpProfileAddToDto(hpProfileAddResponseTO);
	}
	
	@Override
	public HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException {
		return iHpProfileMapper.HpProfilePictureUploadToDto(hpProfileService.uploadHpProfilePhoto(file, hpProfileId));
	}
}
