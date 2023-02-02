package in.gov.abdm.nmr.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;

@Service
public class HpRegistrationServiceImpl implements IHpRegistrationService {
	@Autowired
	private IHpProfileMapper iHpProfileMapper;
	
	@Autowired
	private IHpProfileMasterMapper iHpProfileAuditMapper;

	@Autowired
	private IWorkFlowService iWorkFlowService;
	
	@Autowired
	private IRequestCounterService requestCounterService;
	
	@Autowired
	private IWorkFlowRepository workFlowRepository;
	
	@Autowired
	private IWorkFlowAuditRepository iWorkFlowAuditRepository;
	
	@Autowired
	private IHpProfileMasterRepository iHpProfileAuditRepository;
	
	@Autowired
	private IHpProfileRepository iHpProfileRepository;
	
	@Autowired
	private IRegistrationDetailRepository registrationDetailRepository;
	
	@Autowired
	private RegistrationDetailMasterRepository registrationDetailAuditRepository;
	
	@Autowired
	private WorkProfileRepository workProfileRepository;
	
	@Autowired
	private WorkProfileMasterRepository workProfileAuditRepository;

	@Autowired
	private IHpProfileDaoService hpProfileDaoService;

	@Autowired
	private IAddressRepository iAddressRepository;

	@Autowired
	private LanguagesKnownRepository languagesKnownRepository;

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private HpNbeDetailsRepository hpNbeDetailsRepository;

	@Autowired
	private IQualificationDetailRepository qualificationDetailRepository;

	@Autowired
	private IForeignQualificationDetailRepository customQualificationDetailRepository;

	@Autowired
	private SuperSpecialityRepository superSpecialityRepository;

	@Autowired
	private BroadSpecialityRepository broadSpecialityRepository;


	@Override
	public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(
			SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
		return iHpProfileMapper
				.SmcRegistrationToDto(hpProfileDaoService.fetchSmcRegistrationDetail(smcRegistrationDetailRequestTO));
	}

	private void addHpProfileInHpProfileAudit(BigInteger hpProfileId) {
		HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);
		HpProfileMaster hpProfileMaster = iHpProfileAuditMapper.hpProfileToHpProfileMaster(hpProfile);
		iHpProfileAuditRepository.save(hpProfileMaster);
	}
	
	private void addRegistrationDetailsInRegistrationAudit(BigInteger hpProfileId) {
		RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
		RegistrationDetailsMaster registrationDetailsAudit = iHpProfileAuditMapper.registrationDetailsToRegistrationDetailsMaster(registrationDetails);
		registrationDetailAuditRepository.save(registrationDetailsAudit);
	}

	private void addWorkProfileToWorkProfileAudit(BigInteger hpProfileId) {
		WorkProfile workProfile = workProfileRepository.getWorkProfileByHpProfileId(hpProfileId);
		WorkProfileMaster workProfileAudit = iHpProfileAuditMapper.workProfileToWorkProfileMaster(workProfile);
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
	public HpProfilePersonalResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
																 HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException {
		HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpPersonalDetails(hpProfileId, hpPersonalUpdateRequestTO);
		return getHealthProfessionalPersonalDetail(hpProfileUpdateResponseTO.getHpProfileId());
	}

	@Override
	public HpProfileRegistrationResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
																	 String hpRegistrationUpdateRequestTO, MultipartFile certificate, MultipartFile proof) {

		HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpRegistrationDetails(hpProfileId, hpRegistrationUpdateRequestTO,certificate,proof);
		return getHealthProfessionalRegistrationDetail(hpProfileUpdateResponseTO.getHpProfileId());
	}

	@Override
	public HpProfileWorkDetailsResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
																  String hpWorkProfileUpdateRequestString, MultipartFile proof) {
		HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateWorkProfileDetails(hpProfileId, hpWorkProfileUpdateRequestString, proof);
		return getHealthProfessionalWorkDetail(hpProfileUpdateResponseTO.getHpProfileId());
	}

	@Override
	public HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO)
			throws InvalidRequestException, WorkFlowException {
		String requestId = null;
		if(hpSubmitRequestTO.getHpProfileId() != null &&
				iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpSubmitRequestTO.getHpProfileId())){
			throw new WorkFlowException("Cant create new request until an existing request is closed.", HttpStatus.BAD_REQUEST);
		}
		if (hpSubmitRequestTO.getRequestId() != null && WorkflowStatus.QUERY_RAISED.getId().equals(workFlowRepository.findByRequestId(hpSubmitRequestTO.getRequestId()).getWorkFlowStatus().getId())) {
			iWorkFlowService.assignQueriesBackToQueryCreator(hpSubmitRequestTO.getRequestId());
		}else {
			requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(hpSubmitRequestTO.getApplicationTypeId()));
			WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
					.applicationTypeId(hpSubmitRequestTO.getApplicationTypeId())
					.hpProfileId(hpSubmitRequestTO.getHpProfileId())
					.actionId(Action.SUBMIT.getId())
					.actorId(Group.HEALTH_PROFESSIONAL.getId())
					.build();
			iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);
			HpProfile hpProfileById = iHpProfileRepository.findHpProfileById(hpSubmitRequestTO.getHpProfileId());
			hpProfileById.setTransactionId(hpSubmitRequestTO.getTransactionId());
			hpProfileById.setESignStatus(hpSubmitRequestTO.getESignStatus());
			hpProfileById.setRequestId(requestId);
			iHpProfileRepository.save(hpProfileById);
		}
		return new HpProfileAddResponseTO(201, "Hp Profile Submitted Successfully!", hpSubmitRequestTO.getHpProfileId(),requestId);
	}

	@Override
	public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(BigInteger hpProfileId) {
		HpProfile hpProfile = hpProfileDaoService.findById(hpProfileId);
		Address communicationAddressByHpProfileId = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, AddressType.COMMUNICATION.getId());
		List<LanguagesKnown> languagesKnown = languagesKnownRepository.getLanguagesKnownByHpProfileId(hpProfileId);
		List<Language> languages = languageRepository.getLanguage();
		return HpPersonalDetailMapper.convertEntitiesToPersonalResponseTo(hpProfile, communicationAddressByHpProfileId, languagesKnown, languages);
	}

	@Override
	public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId) {
		BigInteger previousHpProfileId = getSecondLastHpProfile(hpProfileId);
		List<SuperSpeciality> superSpecialities = NMRUtil.coalesceCollection(superSpecialityRepository.getSuperSpecialityFromHpProfileId(hpProfileId), superSpecialityRepository.getSuperSpecialityFromHpProfileId(previousHpProfileId));
		WorkProfile workProfile = NMRUtil.coalesce(workProfileRepository.getWorkProfileByHpProfileId(hpProfileId), workProfileRepository.getWorkProfileByHpProfileId(previousHpProfileId));
		HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = HpProfileWorkProfileMapper.convertEntitiesToWorkDetailResponseTo(superSpecialities, workProfile);
		hpProfileWorkDetailsResponseTO.setHpProfileId(hpProfileId);
		return hpProfileWorkDetailsResponseTO;
	}

	@Override
	public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(BigInteger hpProfileId) {
		BigInteger previousHpProfileId = getSecondLastHpProfile(hpProfileId);
		RegistrationDetails registrationDetails = NMRUtil.coalesce(registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId),
				registrationDetailRepository.getRegistrationDetailsByHpProfileId(previousHpProfileId));
		HpNbeDetails nbeDetails = NMRUtil.coalesce(hpNbeDetailsRepository.findByHpProfileId(hpProfileId),hpNbeDetailsRepository.findByHpProfileId(previousHpProfileId));
		List<QualificationDetails> indianQualifications = NMRUtil.coalesceCollection(qualificationDetailRepository.getQualificationDetailsByHpProfileId(hpProfileId),
				qualificationDetailRepository.getQualificationDetailsByHpProfileId(previousHpProfileId));
		List<ForeignQualificationDetails> internationalQualifications =  NMRUtil.coalesceCollection(customQualificationDetailRepository.getQualificationDetailsByHpProfileId(hpProfileId),
		customQualificationDetailRepository.getQualificationDetailsByHpProfileId(previousHpProfileId));
		HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = HpProfileRegistrationMapper.convertEntitiesToRegistrationResponseTo(registrationDetails, nbeDetails, indianQualifications, internationalQualifications);
		hpProfileRegistrationResponseTO.setHpProfileId(hpProfileId);
		return  hpProfileRegistrationResponseTO;
	}

	private BigInteger getSecondLastHpProfile(BigInteger hpProfileId){
		HpProfile secondLastHpProfile = iHpProfileRepository.findSecondLastHpProfile(iHpProfileRepository.findById(hpProfileId).get().getRegistrationId());
		if(secondLastHpProfile != null){
			return secondLastHpProfile.getId();
		}
		return BigInteger.ZERO;

	}
}
