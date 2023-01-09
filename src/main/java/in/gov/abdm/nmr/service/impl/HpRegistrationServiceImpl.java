package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.entity.WorkFlowAudit;
import in.gov.abdm.nmr.entity.WorkFlowStatus;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.IHpProfileMapper;
import in.gov.abdm.nmr.repository.IWorkFlowAuditRepository;
import in.gov.abdm.nmr.repository.IWorkFlowRepository;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;

@Service
public class HpRegistrationServiceImpl implements IHpRegistrationService {

	private HpProfileDaoServiceImpl hpProfileService;

	private IHpProfileMapper iHpProfileMapper;

	@Autowired
	private IWorkFlowService iWorkFlowService;
	@Autowired
	private IRequestCounterService requestCounterService;
	@Autowired
	private IWorkFlowRepository workFlowRepository;
	
	@Autowired
	private IWorkFlowAuditRepository iWorkFlowAuditRepository;

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
		
		if (workFlowRepository.findByRequestId(hpProfileUpdateRequest.getRequestId()).getWorkFlowStatus().equals(WorkflowStatus.QUERY_RAISED.getId())) {
			
			HpProfileUpdateResponseTO hpProfileUpdateResponseTO = iHpProfileMapper
					.HpProfileUpdateToDto(hpProfileService.updateHpProfile(hpProfileId, hpProfileUpdateRequest));
			
			iWorkFlowService.assignQueriesBackToQueryCreator(hpProfileUpdateRequest.getRequestId());
		}
		if(iWorkFlowService.isAnyApprovedWorkflowForHealthProfessional(hpProfileId)) { 
			String requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.HP_MODIFICATION.getId()));

			WorkFlowRequestTO approvedWorkFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
					.applicationTypeId(ApplicationType.HP_MODIFICATION.getId())
					.hpProfileId(hpProfileId)
					.actionId(Action.SUBMIT.getId())
					.actorId(Group.HEALTH_PROFESSIONAL.getId())
					.build();
			iWorkFlowService.initiateSubmissionWorkFlow(approvedWorkFlowRequestTO);		
			
		}
		//if approved profile
		//new modificaiton worflow
		//copy data from main table to audit table.
		//update data in main table along with new request id.
		//initiate new workflow.
		return null;
//		return hpProfileUpdateResponseTO;
	}

	@Override
	public HpProfileAddResponseTO addHpProfileDetail(HpProfileAddRequestTO hpProfileAddRequestTO)
			throws InvalidRequestException, WorkFlowException {
		if(hpProfileAddRequestTO.getRegistrationDetail().getHpProfileId() != null &&
				iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpProfileAddRequestTO.getRegistrationDetail().getHpProfileId())){
			throw new WorkFlowException("Cant create new request until an existing request is closed.", HttpStatus.BAD_REQUEST);
		}
		HpProfileAddResponseTO hpProfileAddResponseTO = hpProfileService.addHpProfile(hpProfileAddRequestTO);
		String requestId = hpProfileAddRequestTO.getRequestId();
		if(hpProfileAddRequestTO.getRequestId() == null){
			requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(ApplicationType.HP_REGISTRATION.getId()));
		}
		WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
				.applicationTypeId(ApplicationType.HP_REGISTRATION.getId())
				.hpProfileId(hpProfileAddResponseTO.getHpProfileId())
				.actionId(Action.SUBMIT.getId())
				.actorId(Group.HEALTH_PROFESSIONAL.getId())
				.build();
		iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
		return iHpProfileMapper
				.HpProfileAddToDto(hpProfileAddResponseTO);
	}
	
	@Override
	public HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException {
		return iHpProfileMapper.HpProfilePictureUploadToDto(hpProfileService.uploadHpProfilePhoto(file, hpProfileId));
	}
}
