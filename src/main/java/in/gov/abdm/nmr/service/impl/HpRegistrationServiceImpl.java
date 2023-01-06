package in.gov.abdm.nmr.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.dto.hpprofile.HpProfileAddRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IHpProfileMapper;

@Service
public class HpRegistrationServiceImpl implements IHpRegistrationService {

	private HpProfileDaoServiceImpl hpProfileService;

	private IHpProfileMapper iHpProfileMapper;

	@Autowired
	private IWorkFlowService iWorkFlowService;

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
		WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(hpProfileAddRequestTO.getRequestId())
				.applicationTypeId(BigInteger.ONE)
				.hpProfileId(hpProfileAddResponseTO.getHpProfileId())
				.actionId(BigInteger.ONE)
				.actorId(BigInteger.ONE)
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
