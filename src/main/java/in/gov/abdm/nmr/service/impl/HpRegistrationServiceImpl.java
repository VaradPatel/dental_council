package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.service.IHpRegistrationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.NO_MATCHING_REGISTRATION_DETAILS_FOUND;
import static in.gov.abdm.nmr.util.NMRConstants.NO_MATCHING_WORK_PROFILE_DETAILS_FOUND;
import static in.gov.abdm.nmr.util.NMRUtil.validateQualificationDetailsAndProofs;
import static in.gov.abdm.nmr.util.NMRUtil.validateWorkProfileDetails;

@Service
@Slf4j
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
    private IHpProfileMasterRepository iHpProfileMasterRepository;

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

    @Autowired
    private IRegistrationDetailRepository iRegistrationDetailRepository;

    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private IStateMedicalCouncilRepository iStateMedicalCouncilRepository;

    @Autowired
    private IHpProfileStatusRepository hpProfileStatusRepository;

    @Autowired
    IAddressMasterRepository iAddressMasterRepository;

    /**
     * This method fetches the SMC registration details for a given request.
     *
     * @param councilId
     * @param registrationNumber
     * @return SmcRegistrationDetailResponseTO A TO (Transfer Object) containing the SMC registration information that was fetched.
     */
    @Override
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber) throws NmrException {
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
    public String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs, List<MultipartFile> proofs) throws NmrException, InvalidRequestException, WorkFlowException {
        HpProfile hpProfile = hpProfileDaoService.findById(hpProfileId);
        if (hpProfile.getNmrId() == null) {
            throw new NmrException("You cannot raise additional qualification request until NMR Id is generated", HttpStatus.BAD_REQUEST);
        }
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
                                                                           HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, MultipartFile registrationCertificate, MultipartFile degreeCertificate) throws InvalidRequestException, NmrException {
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpRegistrationDetails(hpProfileId, hpRegistrationUpdateRequestTO, registrationCertificate, degreeCertificate);
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

            List<QualificationDetails> qualificationDetailsList = new ArrayList<>();
            if (hpSubmitRequestTO.getApplicationTypeId().equals(ApplicationType.HP_REGISTRATION.getId())) {
                List<QualificationDetails> qualificationDetails = qualificationDetailRepository.getQualificationDetailsByHpProfileId(hpSubmitRequestTO.getHpProfileId());
                String finalRequestId1 = requestId;
                qualificationDetails.forEach(qualifications -> {
                    qualifications.setRequestId(finalRequestId1);
                    qualificationDetailsList.add(qualifications);
                });
                qualificationDetailRepository.saveAll(qualificationDetailsList);
            }
        }
        return new HpProfileAddResponseTO(201, "Hp Profile Submitted Successfully!", hpSubmitRequestTO.getHpProfileId(), requestId);
    }

    @Override
    public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(BigInteger hpProfileId) {
        HpProfile hpProfile = hpProfileDaoService.findById(hpProfileId);
        Address communicationAddressByHpProfileId = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, AddressType.COMMUNICATION.getId());
        Address kycAddressByHpProfileId = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, AddressType.KYC.getId());
        BigInteger applicationTypeId = null;
        BigInteger workFlowStatusId = null;
        if (hpProfile.getRequestId() != null) {
            WorkFlow workFlow = workFlowRepository.findByRequestId(hpProfile.getRequestId());
            if (workFlow != null) {
                applicationTypeId = workFlow.getApplicationType().getId();
                workFlowStatusId = workFlow.getWorkFlowStatus().getId();
            }
        }
        return HpPersonalDetailMapper.convertEntitiesToPersonalResponseTo(hpProfile, communicationAddressByHpProfileId, kycAddressByHpProfileId, applicationTypeId, workFlowStatusId);
    }

    @Override
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId) throws NmrException {
        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = null;
        List<SuperSpeciality> superSpecialities = NMRUtil.coalesceCollection(superSpecialityRepository.getSuperSpecialityFromHpProfileId(hpProfileId), superSpecialityRepository.getSuperSpecialityFromHpProfileId(hpProfileId));
        List<String> registrationNos = iRegistrationDetailRepository.getRegistrationNosByHpProfileId(hpProfileId);

        List<WorkProfile> workProfileList = new ArrayList<>();
        if (!registrationNos.isEmpty()) {
            workProfileList = workProfileRepository.getWorkProfileDetailsByRegNo(registrationNos.get(0));
        } else {
            throw new NmrException(NO_MATCHING_REGISTRATION_DETAILS_FOUND, HttpStatus.NOT_FOUND);
        }

        if (!workProfileList.isEmpty()) {
            hpProfileWorkDetailsResponseTO = HpProfileWorkProfileMapper.convertEntitiesToWorkDetailResponseTo(superSpecialities, workProfileList);
        } else {
            throw new NmrException(NO_MATCHING_WORK_PROFILE_DETAILS_FOUND, HttpStatus.NOT_FOUND);
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

    @Transactional
    public KycResponseMessageTo saveUserKycDetails(String registrationNumber, UserKycTo userKycTo) {

        HpProfile hpProfile = iHpProfileRepository.findLatestHpProfileByRegistrationId(registrationNumber);

        if (hpProfile != null) {

            List<FuzzyParameter> fuzzyParameters = new ArrayList<>();
            double nameMatch = getFuzzyScore(hpProfile.getFullName(), userKycTo.getName());
            boolean isNameMatching = nameMatch > NMRConstants.FUZZY_MATCH_LIMIT;
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_NAME, hpProfile.getFullName(), userKycTo.getName(), isNameMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));

            double genderMatch = getFuzzyScore(hpProfile.getGender(), userKycTo.getGender());
            boolean isGenderMatching = genderMatch > NMRConstants.FUZZY_MATCH_LIMIT;
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_GENDER, hpProfile.getGender(), userKycTo.getGender(), isGenderMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            boolean isDobMatching = (s.format(hpProfile.getDateOfBirth()).compareTo(s.format(userKycTo.getBirthDate()))) == 0;
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_DOB, hpProfile.getDateOfBirth().toString(), userKycTo.getBirthDate().toString(), isDobMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));


            if (isNameMatching && isDobMatching && isGenderMatching) {
                UserKyc existingUserKyc = userKycRepository.findUserKycByRegistrationNumber(registrationNumber);
                if (existingUserKyc != null) {
                    userKycTo.setId(existingUserKyc.getId());
                }
                userKycTo.setHpProfileId(hpProfile.getId());
                userKycTo.setRegistrationNo(registrationNumber);
                userKycRepository.save(userKycDtoMapper.userKycToToUserKyc(userKycTo));

                Address existingAddress = addressRepository.getCommunicationAddressByHpProfileId(hpProfile.getId(), AddressType.KYC.getId());
                Address address = new Address();
                if (existingAddress != null) {
                    address.setId(existingAddress.getId());
                }
                address.setPincode(userKycTo.getPincode());
                address.setMobile(userKycTo.getMobileNumber());
                address.setAddressLine1(userKycTo.getAddress());
                address.setEmail(userKycTo.getEmail());
                address.setHouse(userKycTo.getHouse());
                address.setStreet(userKycTo.getStreet());
                address.setLocality(userKycTo.getLocality());
                address.setLandmark(userKycTo.getLandmark());
                address.setHpProfileId(hpProfile.getId());
                address.setAddressTypeId(new in.gov.abdm.nmr.entity.AddressType(AddressType.KYC.getId(), AddressType.KYC.name()));
                address.setVillage(villagesRepository.findByName(userKycTo.getVillageTownCity()));
                address.setSubDistrict(subDistrictRepository.findByName(userKycTo.getSubDist()));
                address.setState(stateRepository.findByName(userKycTo.getState().toUpperCase()));
                address.setDistrict(districtRepository.findByDistrictNameAndStateId(userKycTo.getDistrict().toUpperCase(), address.getState().getId()));
                address.setCountry(stateRepository.findByName(userKycTo.getState().toUpperCase()).getCountry());
                iAddressRepository.save(address);
                hpProfile.setProfilePhoto(userKycTo.getPhoto().getBytes());
                iHpProfileRepository.save(hpProfile);

                return new KycResponseMessageTo(fuzzyParameters, NMRConstants.SUCCESS_RESPONSE);
            } else {
                return new KycResponseMessageTo(fuzzyParameters, NMRConstants.FAILURE_RESPONSE);
            }
        } else {
            return new KycResponseMessageTo(Collections.emptyList(), NMRConstants.USER_NOT_FOUND);
        }
    }

    @Override
    public void addNewHealthProfessional(NewHealthPersonalRequestTO request) throws DateException {
        HpProfile hpProfile = new HpProfile();
        hpProfile.setAadhaarToken(request.getAadhaarToken() != null ? request.getAadhaarToken() : null);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date dt = df.parse(request.getBirthdate());
            hpProfile.setDateOfBirth(new java.sql.Date(dt.getTime()));
        } catch (ParseException e) {
            throw new DateException(NMRError.DATE_EXCEPTION.getCode(), NMRError.DATE_EXCEPTION.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
        hpProfile.setEmailId(request.getEmail() != null ? request.getEmail() : null);
        hpProfile.setFullName(request.getName());
        hpProfile.setGender(request.getGender());
        hpProfile.setMobileNumber(request.getMobileNumber() != null ? request.getMobileNumber() : null);
        hpProfile.setSalutation(NMRConstants.SALUTATION_DR);
        hpProfile.setProfilePhoto(request.getPhoto() != null ? request.getPhoto().getBytes() : null);
        hpProfile.setRegistrationId(request.getRegistrationNumber());
        hpProfile.setIsSameAddress(String.valueOf(false));
        hpProfile.setCountryNationality(countryRepository.findByName(NMRConstants.DEFAULT_COUNTRY_AADHAR));
        hpProfile.setIsNew(1);
        hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(HpProfileStatus.PENDING.getId()).get());
        hpProfile = iHpProfileRepository.save(hpProfile);

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setStateMedicalCouncil(iStateMedicalCouncilRepository.findById(new BigInteger(request.getSmcId())).get());
        registrationDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetails.setRegistrationNo(request.getRegistrationNumber());
        registrationDetails.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetails.setHpProfileId(hpProfile);
        iRegistrationDetailRepository.save(registrationDetails);

        Address address = new Address();
        address.setPincode(request.getPincode());
        address.setMobile(request.getMobileNumber());
        address.setAddressLine1(request.getAddress());
        address.setEmail(request.getEmail() != null ? request.getEmail() : null);
        address.setHouse(request.getHouse() != null ? request.getHouse() : null);
        address.setStreet(request.getStreet() != null ? request.getStreet() : null);
        address.setLocality(request.getLocality() != null ? request.getLocality() : null);
        address.setHouse(request.getHouse() != null ? request.getHouse() : null);
        address.setLandmark(request.getLandmark() != null ? request.getLandmark() : null);
        address.setLandmark(request.getLandmark() != null ? request.getLandmark() : null);
        address.setHpProfileId(hpProfile.getId());
        address.setAddressTypeId(new in.gov.abdm.nmr.entity.AddressType(AddressType.KYC.getId(), AddressType.KYC.name()));
        address.setVillage(request.getVillageTownCity() != null ? villagesRepository.findByName(request.getLocality()) : null);
        address.setSubDistrict(request.getSubDist() != null ? subDistrictRepository.findByName(request.getSubDist()) : null);
        address.setState(stateRepository.findByName(request.getState().toUpperCase()));
        address.setDistrict(districtRepository.findByDistrictNameAndStateId(request.getDistrict().toUpperCase(), address.getState().getId()));
        address.setCountry(stateRepository.findByName(request.getState().toUpperCase()).getCountry());
        iAddressRepository.save(address);
    }

    @Override
    public void updateHealthProfessionalEMailMobile(BigInteger hpProfileId, EmailMobileTO request) {
        if (request.getEmail() != null) {
            iHpProfileRepository.updateHpProfileEmail(hpProfileId, request.getEmail());
            iAddressRepository.updateAddressEmail(hpProfileId, request.getEmail(), AddressType.COMMUNICATION.getId());
        }
        if (request.getMobileNumber() != null) {
            iHpProfileRepository.updateHpProfileMobile(hpProfileId, request.getMobileNumber());
        }
        BigInteger masterHpProfileId = iHpProfileRepository.findMasterHpProfileByHpProfileId(hpProfileId);
        if (masterHpProfileId != null) {
            if (request.getEmail() != null) {
                iHpProfileMasterRepository.updateMasterHpProfileEmail(masterHpProfileId, request.getEmail());
                iAddressMasterRepository.updateMasterAddressEmail(masterHpProfileId, request.getEmail(), AddressType.COMMUNICATION.getId());
            }
            if (request.getMobileNumber() != null) {// update mobile_number hp_profile_master by hp_profile_master.id
                iHpProfileMasterRepository.updateMasterHpProfileMobile(masterHpProfileId, request.getMobileNumber());
            }
        }
    }

    /**
     * This method implements the string comparsion logic based on the fuzzy
     * logic.
     *
     * @param s1 is the string which need to compare.
     * @param s2 is the string which needs to compare.
     * @return this will return the double value 0/1,0 means the strings are
     * not matched,
     * And 1 represents the strings are equal.
     */
    public double getFuzzyScore(String s1, String s2) {
        double score;
        if (new Soundex().soundex(s1).equals(s2)) {
            log.info("Matched using soundex algorithm");
            return 100;
        }
        if (new Metaphone().isMetaphoneEqual(s1, s2)) {
            log.info("Matched using metaphone algorithm");
            return 100;
        }
        return (100 - (new LevenshteinDistance().apply(s1, s2) / (double) s2.length()) * 100);
    }
}
