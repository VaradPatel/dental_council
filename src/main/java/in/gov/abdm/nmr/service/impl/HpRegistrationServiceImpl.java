package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.entity.RequestCounter;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.HP_PROFILE_STATUS;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.IHpProfileMapper;
import in.gov.abdm.nmr.repository.IApplicationTypeRepository;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
public class HpRegistrationServiceImpl implements IHpRegistrationService {

	private HpProfileDaoServiceImpl hpProfileService;

	private IHpProfileMapper iHpProfileMapper;

	private static final List<BigInteger> NEW_REQUEST_CREATION_STATUS_ID = List.of(HP_PROFILE_STATUS.REJECTED.getId(),
			HP_PROFILE_STATUS.APPROVED.getId());

	@Autowired
	private IWorkFlowService iWorkFlowService;
	@Autowired
	private IRequestCounterService requestCounterService;

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
			HpProfileUpdateRequestTO hpProfileUpdateRequest) throws InvalidRequestException {
		return iHpProfileMapper
				.HpProfileUpdateToDto(hpProfileService.updateHpProfile(hpProfileId, hpProfileUpdateRequest));
	}

	@Override
	public HpProfileAddResponseTO addHpProfileDetail(HpProfileAddRequestTO hpProfileAddRequestTO)
			throws InvalidRequestException, WorkFlowException {
		HpProfileAddResponseTO hpProfileAddResponseTO = hpProfileService.addHpProfile(hpProfileAddRequestTO);
		String requestId = hpProfileAddRequestTO.getRequestId();
		if(hpProfileAddRequestTO.getRequestId() == null){
			RequestCounter requestCounter = requestCounterService.incrementAndRetrieveCount(ApplicationType.HP_REGISTRATION.getId());
			requestId = requestCounter.getApplicationType().getRequestPrefixId().concat(String.valueOf(requestCounter.getCounter()));
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
