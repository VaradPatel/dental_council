package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRUtil.validateQualificationDetailsAndProofs;
import static in.gov.abdm.nmr.util.NMRUtil.validateWorkProfileDetails;

@Service
public class HpRegistrationServiceImpl implements IHpRegistrationService {
    @Autowired
    private IHpProfileMapper iHpProfileMapper;

    @Autowired
    private UserKycDtoMapper userKycDtoMapper;

    @Autowired
    private CountryDtoMapper countryDtoMapper;

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
    private VillagesRepository villagesRepository;

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

    @Autowired
    private UserKycRepository userKycRepository;

    @Autowired
    private IStateRepository stateRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private SubDistrictRepository subDistrictRepository;

    /**
     * This method fetches the SMC registration details for a given request.
     *
     * @param councilId
     * @param registrationNumber
     * @return SmcRegistrationDetailResponseTO A TO (Transfer Object) containing the SMC registration information that was fetched.
     */
    @Override
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber) {
        return iHpProfileMapper
                .smcRegistrationToDto(hpProfileDaoService.fetchSmcRegistrationDetail(councilId, registrationNumber));
    }


    @Override
    public HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException {
        return iHpProfileMapper.hpProfilePictureUploadToDto(hpProfileDaoService.uploadHpProfilePhoto(file, hpProfileId));
    }

    /**
     * Adds a list of qualification details to the specified health professional's profile.
     *
     * @param hpProfileId                   The ID of the health professional's profile.
     * @param qualificationDetailRequestTOs A list of qualification detail requests as a string.
     * @return The string "Success" if the operation is successful.
     * @throws WorkFlowException If an error occurs while initiating the submission workflow.
     */
    @Override
    public String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs, List<MultipartFile> proofs) throws WorkFlowException, InvalidRequestException {
        validateQualificationDetailsAndProofs(qualificationDetailRequestTOs, proofs);
        for (QualificationDetailRequestTO qualificationDetailRequestTO : qualificationDetailRequestTOs) {
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
        hpProfileDaoService.saveQualificationDetails(hpProfileDaoService.findById(hpProfileId), null, qualificationDetailRequestTOs, proofs);
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
                                                                           HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, MultipartFile certificate, MultipartFile proof, List<MultipartFile> proofOfQualifications) throws InvalidRequestException, NmrException {
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpRegistrationDetails(hpProfileId, hpRegistrationUpdateRequestTO, certificate, proof, proofOfQualifications);
        return getHealthProfessionalRegistrationDetail(hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Override
    public HpProfileWorkDetailsResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
                                                                       HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<MultipartFile> proofs) throws NmrException, InvalidRequestException {
        validateWorkProfileDetails(hpWorkProfileUpdateRequestTO.getCurrentWorkDetails());
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateWorkProfileDetails(hpProfileId, hpWorkProfileUpdateRequestTO, proofs);
        return getHealthProfessionalWorkDetail(hpProfileUpdateResponseTO.getHpProfileId());
    }

    @Override
    public HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO)
            throws InvalidRequestException, WorkFlowException {
        String requestId = null;
        if (hpSubmitRequestTO.getHpProfileId() != null &&
                iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpSubmitRequestTO.getHpProfileId())) {
            throw new WorkFlowException("Cant create new request until an existing request is closed.", HttpStatus.BAD_REQUEST);
        }
        if (hpSubmitRequestTO.getRequestId() != null && WorkflowStatus.QUERY_RAISED.getId().equals(workFlowRepository.findByRequestId(hpSubmitRequestTO.getRequestId()).getWorkFlowStatus().getId())) {
            iWorkFlowService.assignQueriesBackToQueryCreator(hpSubmitRequestTO.getRequestId());
        } else {
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

            RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpSubmitRequestTO.getHpProfileId());
            registrationDetails.setRequestId(requestId);

            List<WorkProfile> workProfileList = new ArrayList<>();
            List<WorkProfile> workProfiles = workProfileRepository.getWorkProfileDetailsByHPId(hpSubmitRequestTO.getHpProfileId());
            String finalRequestId = requestId;
            workProfiles.forEach(workProfile -> {
                workProfile.setRequestId(finalRequestId);
                workProfileList.add(workProfile);
            });
            registrationDetailRepository.save(registrationDetails);
            workProfileRepository.saveAll(workProfileList);
            iHpProfileRepository.save(hpProfileById);
        }
        return new HpProfileAddResponseTO(201, "Hp Profile Submitted Successfully!", hpSubmitRequestTO.getHpProfileId(), requestId);
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
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId) throws NmrException {
        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = null;
        List<SuperSpeciality> superSpecialities = NMRUtil.coalesceCollection(superSpecialityRepository.getSuperSpecialityFromHpProfileId(hpProfileId), superSpecialityRepository.getSuperSpecialityFromHpProfileId(hpProfileId));
        List<WorkProfile> workProfileList = workProfileRepository.getWorkProfileDetailsByHPId(hpProfileId);
        if (!workProfileList.isEmpty()) {
            hpProfileWorkDetailsResponseTO = HpProfileWorkProfileMapper.convertEntitiesToWorkDetailResponseTo(superSpecialities, workProfileList);
        } else {
            throw new NmrException("Invalid HP profile ID", HttpStatus.valueOf(404));
        }
        return hpProfileWorkDetailsResponseTO;
    }

    @Override
    public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(BigInteger hpProfileId) {

        RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
        HpNbeDetails nbeDetails = hpNbeDetailsRepository.findByHpProfileId(hpProfileId);
        List<QualificationDetails> indianQualifications = qualificationDetailRepository.getQualificationDetailsByHpProfileId(hpProfileId);
        List<ForeignQualificationDetails> internationalQualifications = customQualificationDetailRepository.getQualificationDetailsByHpProfileId(hpProfileId);
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = HpProfileRegistrationMapper.convertEntitiesToRegistrationResponseTo(registrationDetails, nbeDetails, indianQualifications, internationalQualifications);
        hpProfileRegistrationResponseTO.setHpProfileId(hpProfileId);
        return hpProfileRegistrationResponseTO;
    }

    public ResponseMessageTo saveUserKycDetails(UserKycTo userKycTo) {

        userKycRepository.save(userKycDtoMapper.userKycToToUserKyc(userKycTo));

        Address address = new Address();
        address.setPincode(userKycTo.getPincode());
        address.setMobile(userKycTo.getMobileNumber());
        address.setAddressLine1(userKycTo.getAddress());
        address.setEmail(userKycTo.getEmail());
        address.setHouse(userKycTo.getHouse());
        address.setStreet(userKycTo.getStreet());
        address.setLocality(userKycTo.getLocality());
        address.setLandmark(userKycTo.getLandmark());
        address.setHpProfileId(userKycTo.getHpProfileId());
        address.setAddressTypeId(new in.gov.abdm.nmr.entity.AddressType(AddressType.KYC.getId(), AddressType.COMMUNICATION.name()));
        address.setVillage(villagesRepository.findByName(userKycTo.getLocality()));
        address.setSubDistrict(subDistrictRepository.findByName(userKycTo.getSubDist()));
        address.setState(stateRepository.findByName(userKycTo.getState()));
        address.setDistrict(districtRepository.findByName(userKycTo.getDistrict().toUpperCase()));
        address.setCountry(stateRepository.findByName(userKycTo.getState().toUpperCase()).getCountry());
        iAddressRepository.save(address);

        return new ResponseMessageTo(null, NMRConstants.SUCCESS);
    }
}
