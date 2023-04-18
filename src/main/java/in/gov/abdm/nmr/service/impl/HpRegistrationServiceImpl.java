package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.hpprofile.HpSubmitRequestTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.nosql.entity.Address;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.nosql.entity.QualificationsDetails;
import in.gov.abdm.nmr.nosql.entity.RegistrationsDetails;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.*;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.*;
import java.util.stream.Collectors;

import static in.gov.abdm.nmr.util.NMRConstants.*;
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

    @Autowired
    private HpProfileRegistrationMapper hpProfileRegistrationMapper;

    @Autowired
    private IQueriesService iQueriesService;

    @Autowired
    OtpServiceImpl otpService;

    @Autowired
    private ICouncilService councilService;

    @Autowired
    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    @Autowired
    private UniversityMasterRepository universityMasterRepository;

    @Autowired
    private IQualificationDetailRepository iQualificationDetailRepository;

    @Autowired
    INotificationService notificationService;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Value("${council.email-verify.url}")
    private String emailVerifyUrl;

    @Autowired
    private IUserDaoService userDaoService;




    /**
     * This method fetches the SMC registration details for a given request.
     *
     * @param councilId
     * @param registrationNumber
     * @return SmcRegistrationDetailResponseTO A TO (Transfer Object) containing the SMC registration information that was fetched.
     */
    @Override
    public SmcRegistrationDetailResponseTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber) throws NmrException, NoDataFoundException {
        return iHpProfileMapper
                .smcRegistrationToDto(hpProfileDaoService.fetchSmcRegistrationDetail(councilId, registrationNumber));
    }


    @Override
    public HpProfilePictureResponseTO uploadHpProfilePicture(MultipartFile file, BigInteger hpProfileId) throws IOException, InvalidRequestException {
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
    @Transactional
    public String addQualification(BigInteger hpProfileId, List<QualificationDetailRequestTO> qualificationDetailRequestTOs, List<MultipartFile> proofs) throws NmrException, InvalidRequestException, WorkFlowException {
        HpProfile hpProfile = hpProfileDaoService.findById(hpProfileId);
        if (hpProfile.getNmrId() == null) {
            throw new WorkFlowException("You cannot raise additional qualification request until NMR Id is generated");
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

    private void mapSuperSpecialityToEntity(BigInteger hpProfileId, SuperSpecialityTO speciality, SuperSpeciality superSpeciality) {
        superSpeciality.setName(speciality.getName());
        superSpeciality.setHpProfileId(hpProfileId);
    }

    @Override
    public HpProfilePersonalResponseTO addOrUpdateHpPersonalDetail(BigInteger hpProfileId,
                                                                   HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException {
        log.info("In HpRegistrationServiceImpl : addOrUpdateHpPersonalDetail method");
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpPersonalDetails(hpProfileId, hpPersonalUpdateRequestTO);

        log.debug("Update Successful. Calling the getHealthProfessionalPersonalDetail method to retrieve the Details. ");
        HpProfilePersonalResponseTO healthProfessionalPersonalDetail = getHealthProfessionalPersonalDetail(hpProfileUpdateResponseTO.getHpProfileId());

        log.info("HpRegistrationServiceImpl: addOrUpdateHpPersonalDetail method: Execution Successful. ");
        return healthProfessionalPersonalDetail;

    }

    @Override
    public HpProfileRegistrationResponseTO addOrUpdateHpRegistrationDetail(BigInteger hpProfileId,
                                                                           HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, MultipartFile registrationCertificate, MultipartFile degreeCertificate) throws InvalidRequestException, NmrException {
        log.info("In HpRegistrationServiceImpl : addOrUpdateHpRegistrationDetail method");
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateHpRegistrationDetails(hpProfileId, hpRegistrationUpdateRequestTO, registrationCertificate, degreeCertificate);

        log.debug("Update Successful. Calling the getHealthProfessionalRegistrationDetail method to retrieve the Details. ");
        HpProfileRegistrationResponseTO healthProfessionalRegistrationDetail = getHealthProfessionalRegistrationDetail(hpProfileUpdateResponseTO.getHpProfileId());

        log.info("HpRegistrationServiceImpl: addOrUpdateHpRegistrationDetail method: Execution Successful. ");
        return healthProfessionalRegistrationDetail;
    }

    @Override
    public HpProfileWorkDetailsResponseTO addOrUpdateWorkProfileDetail(BigInteger hpProfileId,
                                                                       HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<MultipartFile> proofs) throws NmrException, InvalidRequestException, NotFoundException {

        log.info("In HpRegistrationServiceImpl : addOrUpdateWorkProfileDetail method");

        validateWorkProfileDetails(hpWorkProfileUpdateRequestTO.getCurrentWorkDetails());

        log.debug("WorkProfileDetails Validation Successful. Calling the updateWorkProfileDetails method to update the work profile details. ");
        HpProfileUpdateResponseTO hpProfileUpdateResponseTO = hpProfileDaoService.updateWorkProfileDetails(hpProfileId, hpWorkProfileUpdateRequestTO, proofs);

        log.debug("Update Successful. Calling the getHealthProfessionalRegistrationDetail method to retrieve the work profile details. ");
        HpProfileWorkDetailsResponseTO healthProfessionalWorkDetail = getHealthProfessionalWorkDetail(hpProfileUpdateResponseTO.getHpProfileId());

        log.info("HpRegistrationServiceImpl: addOrUpdateWorkProfileDetail method: Execution Successful. ");
        return healthProfessionalWorkDetail;

    }

    @Transactional
    @Override
    public HpProfileAddResponseTO submitHpProfile(HpSubmitRequestTO hpSubmitRequestTO)
            throws InvalidRequestException, WorkFlowException {

        log.info("In HpRegistrationServiceImpl : submitHpProfile method");
        String requestId = null;
        if (hpSubmitRequestTO.getHpProfileId() != null &&
                iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(hpSubmitRequestTO.getHpProfileId())) {
            throw new WorkFlowException("Cant create new request until an existing request is closed.");
        }
        WorkFlow lastWorkFlowForHealthProfessional = workFlowRepository.findLastWorkFlowForHealthProfessional(hpSubmitRequestTO.getHpProfileId());
        if (lastWorkFlowForHealthProfessional != null && WorkflowStatus.QUERY_RAISED.getId().equals(lastWorkFlowForHealthProfessional.getWorkFlowStatus().getId())) {
            log.debug("Calling assignQueriesBackToQueryCreator method since there is an existing workflow with 'Query Raised' work flow status. ");
            iWorkFlowService.assignQueriesBackToQueryCreator(lastWorkFlowForHealthProfessional.getRequestId());
            iQueriesService.markQueryAsClosed(hpSubmitRequestTO.getHpProfileId());

        } else {
            log.debug("Proceeding to submit the profile since request_id is not given as a part of input payload");
            requestId = NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(hpSubmitRequestTO.getApplicationTypeId()));
            log.debug("New Request id is built - " + requestId);
            WorkFlowRequestTO workFlowRequestTO = WorkFlowRequestTO.builder().requestId(requestId)
                    .applicationTypeId(hpSubmitRequestTO.getApplicationTypeId())
                    .hpProfileId(hpSubmitRequestTO.getHpProfileId())
                    .actionId(Action.SUBMIT.getId())
                    .actorId(Group.HEALTH_PROFESSIONAL.getId())
                    .build();
            log.debug("Initiating Submission Workflow");
            iWorkFlowService.initiateSubmissionWorkFlow(workFlowRequestTO);

            log.debug("Updating the request_id, e-sign status and transaction id in hp_profile table");
            HpProfile hpProfileById = iHpProfileRepository.findHpProfileById(hpSubmitRequestTO.getHpProfileId());
            hpProfileById.setTransactionId(hpSubmitRequestTO.getTransactionId());
            hpProfileById.setESignStatus(hpSubmitRequestTO.getESignStatus());
            hpProfileById.setRequestId(requestId);

            log.debug("Updating the request_id in registration_details table");
            RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpSubmitRequestTO.getHpProfileId());
            registrationDetails.setRequestId(requestId);

            registrationDetailRepository.save(registrationDetails);
            iHpProfileRepository.save(hpProfileById);

            List<QualificationDetails> qualificationDetailsList = new ArrayList<>();
            if (hpSubmitRequestTO.getApplicationTypeId().equals(ApplicationType.HP_REGISTRATION.getId())) {
                log.debug("Updating the request_id in qualification_details table");
                List<QualificationDetails> qualificationDetails = qualificationDetailRepository.getQualificationDetailsByUserId(hpProfileById.getUser().getId());
                String finalRequestId1 = requestId;
                qualificationDetails.forEach(qualifications -> {
                    qualifications.setRequestId(finalRequestId1);
                    qualificationDetailsList.add(qualifications);
                });
                qualificationDetailRepository.saveAll(qualificationDetailsList);
            }
            List<ForeignQualificationDetails> foreignQualificationDetails = new ArrayList<>();
            if (hpSubmitRequestTO.getApplicationTypeId().equals(ApplicationType.FOREIGN_HP_REGISTRATION.getId())) {
                log.debug("Updating the request_id in foreign_qualification_details table");
                List<ForeignQualificationDetails> qualificationDetails = customQualificationDetailRepository.getQualificationDetailsByUserId(hpProfileById.getUser().getId());
                String finalRequestId1 = requestId;
                qualificationDetails.forEach(qualifications -> {
                    qualifications.setRequestId(finalRequestId1);
                    foreignQualificationDetails.add(qualifications);
                });
                customQualificationDetailRepository.saveAll(foreignQualificationDetails);
            }
        }

        log.info("HpRegistrationServiceImpl: submitHpProfile method: Execution Successful. ");
        return new HpProfileAddResponseTO(201, "Hp Profile Submitted Successfully!", hpSubmitRequestTO.getHpProfileId(), requestId);
    }

    @Override
    public HpProfilePersonalResponseTO getHealthProfessionalPersonalDetail(BigInteger hpProfileId) {
        HpProfile hpProfile = hpProfileDaoService.findById(hpProfileId);
        in.gov.abdm.nmr.entity.Address communicationAddressByHpProfileId = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, AddressType.COMMUNICATION.getId());
        in.gov.abdm.nmr.entity.Address kycAddressByHpProfileId = iAddressRepository.getCommunicationAddressByHpProfileId(hpProfileId, AddressType.KYC.getId());
        BigInteger applicationTypeId = null;
        BigInteger workFlowStatusId = null;
        String requestId = null;
        WorkFlow workFlow = workFlowRepository.findLastWorkFlowForHealthProfessional(hpProfileId);
        if (workFlow != null) {
            applicationTypeId = workFlow.getApplicationType().getId();
            workFlowStatusId = workFlow.getWorkFlowStatus().getId();
            requestId = workFlow.getRequestId();

        }
        return HpPersonalDetailMapper.convertEntitiesToPersonalResponseTo(hpProfile, communicationAddressByHpProfileId, kycAddressByHpProfileId, applicationTypeId, workFlowStatusId, requestId);
    }

    @Override
    public HpProfileWorkDetailsResponseTO getHealthProfessionalWorkDetail(BigInteger hpProfileId) throws NmrException, InvalidRequestException {
        HpProfileWorkDetailsResponseTO hpProfileWorkDetailsResponseTO = null;
        List<WorkProfile> workProfileList = new ArrayList<>();
        List<LanguagesKnown> languagesKnown = new ArrayList<>();
        List<BigInteger> languagesKnownIds = new ArrayList<>();
        Optional<HpProfile> hpProfileOptional = iHpProfileRepository.findById(hpProfileId);
        if (hpProfileOptional.isPresent()) {
            User user = hpProfileOptional.get().getUser();
            if (user != null) {
                BigInteger userId = user.getId();
                workProfileList = workProfileRepository.getWorkProfileDetailsByUserId(userId);
                languagesKnown = languagesKnownRepository.findByUserId(userId);
            } else {
                throw new InvalidRequestException(NO_MATCHING_USER_DETAILS_FOUND);
            }
        }

        if (languagesKnown != null && !languagesKnown.isEmpty()) {
            languagesKnownIds = languagesKnown.stream().map(languageKnown -> {
                Language language = languageKnown.getLanguage();
                return language != null ? language.getId() : null;
            }).collect(Collectors.toList());
        }

        if (!workProfileList.isEmpty()) {
            hpProfileWorkDetailsResponseTO = HpProfileWorkProfileMapper.convertEntitiesToWorkDetailResponseTo(workProfileList);
            hpProfileWorkDetailsResponseTO.setLanguagesKnownIds(languagesKnownIds);
        } else {
            throw new InvalidRequestException(NO_MATCHING_WORK_PROFILE_DETAILS_FOUND);
        }
        return hpProfileWorkDetailsResponseTO;
    }

    @Override
    public HpProfileRegistrationResponseTO getHealthProfessionalRegistrationDetail(BigInteger hpProfileId) {

        RegistrationDetails registrationDetails = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
        HpNbeDetails nbeDetails = hpNbeDetailsRepository.findByUserId(registrationDetails.getHpProfileId().getUser().getId());
        List<QualificationDetails> indianQualifications = qualificationDetailRepository.getQualificationDetailsByUserId(registrationDetails.getHpProfileId().getUser().getId());
        List<ForeignQualificationDetails> internationalQualifications = customQualificationDetailRepository.getQualificationDetailsByUserId(registrationDetails.getHpProfileId().getUser().getId());
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = hpProfileRegistrationMapper.convertEntitiesToRegistrationResponseTo(registrationDetails, nbeDetails, indianQualifications, internationalQualifications);
        hpProfileRegistrationResponseTO.setHpProfileId(hpProfileId);
        return hpProfileRegistrationResponseTO;
    }

    @Transactional
    public KycResponseMessageTo userKycFuzzyMatch(String registrationNumber, BigInteger councilId, UserKycTo userKycTo) throws ParseException {

        StateMedicalCouncil stateMedicalCouncil = stateMedicalCouncilRepository.findStateMedicalCouncilById(councilId);

        List<Council> councils = councilService.getCouncilByRegistrationNumberAndCouncilName(registrationNumber, stateMedicalCouncil.getName());

        Council council = councils.isEmpty() ? null : councils.get(0);

        if (council != null) {

            List<FuzzyParameter> fuzzyParameters = new ArrayList<>();
            double nameMatch = getFuzzyScore(council.getFullName(), userKycTo.getName());
            boolean isNameMatching = nameMatch > NMRConstants.FUZZY_MATCH_LIMIT;
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_NAME, council.getFullName(), userKycTo.getName(), isNameMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));

            double genderMatch = getFuzzyScore(council.getGender(), userKycTo.getGender());
            boolean isGenderMatching = genderMatch > NMRConstants.FUZZY_MATCH_LIMIT;
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_GENDER, council.getGender(), userKycTo.getGender(), isGenderMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");

            boolean isDobMatching;
            try {
                Date dateOfBirth = simpleDateFormat.parse(council.getDateOfBirth());
                isDobMatching = (s.format(dateOfBirth).compareTo(s.format(userKycTo.getBirthDate()))) == 0;
            } catch (ParseException e) {
                log.info("Exception occurred while parsing dob" + e.getMessage());
                isDobMatching = false;
            }
            fuzzyParameters.add(new FuzzyParameter(NMRConstants.FUZZY_PARAMETER_DOB, council.getDateOfBirth().toString(), userKycTo.getBirthDate().toString(), isDobMatching ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE));
            if (isNameMatching && isDobMatching && isGenderMatching) {
                return new KycResponseMessageTo(fuzzyParameters, NMRConstants.SUCCESS_RESPONSE);
            } else {
                return new KycResponseMessageTo(fuzzyParameters, NMRConstants.FAILURE_RESPONSE);
            }
        } else {
            return new KycResponseMessageTo(Collections.emptyList(), NMRConstants.USER_NOT_FOUND);
        }
    }

    @Override
    public void addNewHealthProfessional(NewHealthPersonalRequestTO request) throws DateException, ParseException {

        StateMedicalCouncil stateMedicalCouncil =
                stateMedicalCouncilRepository.findStateMedicalCouncilById(BigInteger.valueOf(Long.parseLong(request.getSmcId())));
        List<Council> councils = councilService.getCouncilByRegistrationNumberAndCouncilName(request.getRegistrationNumber(), stateMedicalCouncil.getName());

        Council council = councils.isEmpty() ? null : councils.get(0);
        RegistrationsDetails imrRegistrationsDetails = null;
        List<QualificationsDetails> qualificationDetailsList = new ArrayList<>();
        if (council != null) {
            imrRegistrationsDetails = council.getRegistrationsDetails().isEmpty() ? null : council.getRegistrationsDetails().get(0);
            for (QualificationsDetails qualificationsDetails : imrRegistrationsDetails.getQualificationsDetails()) {
                if (qualificationsDetails.getName().replaceAll(DOCTOR_QUALIFICATION_PATTERN, "").equalsIgnoreCase(DOCTOR_QUALIFICATION)) {
                    qualificationDetailsList.add(qualificationsDetails);
                }
            }
        }

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
        hpProfile.setProfilePhoto(request.getPhoto() != null ? new String(Base64.getDecoder().decode(request.getPhoto())) : null);
        hpProfile.setRegistrationId(request.getRegistrationNumber());
        hpProfile.setIsSameAddress(String.valueOf(false));
        hpProfile.setCountryNationality(countryRepository.findByName(NMRConstants.DEFAULT_COUNTRY_AADHAR));
        hpProfile.setIsNew(NMRConstants.YES);
        hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(HpProfileStatus.PENDING.getId()).get());
        hpProfile = iHpProfileRepository.save(hpProfile);

        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setStateMedicalCouncil(stateMedicalCouncil);
        registrationDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetails.setRegistrationNo(request.getRegistrationNumber());
        registrationDetails.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetails.setHpProfileId(hpProfile);

        if (imrRegistrationsDetails != null) {
            boolean isRenewable = imrRegistrationsDetails.isRenewableRegistration();
            if (isRenewable) {
                registrationDetails.setIsRenewable("1");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
                registrationDetails.setRenewableRegistrationDate(simpleDateFormat.parse(imrRegistrationsDetails.getRenewableRegistrationDate() != null ?
                        imrRegistrationsDetails.getRenewableRegistrationDate() : null));

            } else {
                registrationDetails.setIsRenewable("0");
                registrationDetails.setRenewableRegistrationDate(null);
            }
        }
        iRegistrationDetailRepository.save(registrationDetails);

        if (!qualificationDetailsList.isEmpty()) {
            QualificationsDetails qualificationsDetails = qualificationDetailsList.get(0);

            QualificationDetails qualificationDetails = new QualificationDetails();
            qualificationDetails.setHpProfile(hpProfile);
            qualificationDetails.setQualificationYear(qualificationsDetails.getQualificationYear() != null ? qualificationsDetails.getQualificationYear() : null);
            qualificationDetails.setQualificationMonth(qualificationsDetails.getQualificationMonth() != null ? qualificationsDetails.getQualificationMonth() : null);
            qualificationDetails.setSystemOfMedicine(qualificationsDetails.getSystemOfMedicine() != null ? qualificationsDetails.getSystemOfMedicine() : null);
            String university = qualificationsDetails.getUniversity();
            if (university != null) {
                UniversityMaster universityMaster = universityMasterRepository.findUniversityByName(university);
                qualificationDetails.setUniversity(universityMaster);
            }
            iQualificationDetailRepository.save(qualificationDetails);
        }

        in.gov.abdm.nmr.entity.Address kycAddress = new in.gov.abdm.nmr.entity.Address();
        kycAddress.setPincode(request.getPincode());
        kycAddress.setMobile(request.getMobileNumber());
        kycAddress.setAddressLine1(request.getAddress());
        kycAddress.setEmail(request.getEmail() != null ? request.getEmail() : null);
        kycAddress.setHouse(request.getHouse() != null ? request.getHouse() : null);
        kycAddress.setStreet(request.getStreet() != null ? request.getStreet() : null);
        kycAddress.setLocality(request.getLocality() != null ? request.getLocality() : null);
        kycAddress.setHouse(request.getHouse() != null ? request.getHouse() : null);
        kycAddress.setLandmark(request.getLandmark() != null ? request.getLandmark() : null);
        kycAddress.setLandmark(request.getLandmark() != null ? request.getLandmark() : null);
        kycAddress.setHpProfileId(hpProfile.getId());
        kycAddress.setAddressTypeId(new in.gov.abdm.nmr.entity.AddressType(AddressType.KYC.getId(), AddressType.KYC.name()));
        kycAddress.setVillage(request.getVillageTownCity() != null ? villagesRepository.findByName(request.getLocality()) : null);
        kycAddress.setSubDistrict(request.getSubDist() != null ? subDistrictRepository.findByName(request.getSubDist()) : null);
        kycAddress.setState(stateRepository.findByName(request.getState().toUpperCase()));
        kycAddress.setDistrict(districtRepository.findByDistrictNameAndStateId(request.getDistrict().toUpperCase(), kycAddress.getState().getId()));
        kycAddress.setCountry(stateRepository.findByName(request.getState().toUpperCase()).getCountry());

        if (council != null) {
            in.gov.abdm.nmr.entity.Address communicationAddressEntity = new in.gov.abdm.nmr.entity.Address();
            Address communicationAddress = new Address();
            for (Address address : council.getAddress()) {
                if (address.getType().equalsIgnoreCase(AddressType.COMMUNICATION.name())) {
                    communicationAddress = address;
                }
            }
            if (communicationAddress != null) {
                communicationAddressEntity.setPincode(communicationAddress.getPincode() != null ? communicationAddress.getPincode() : null);
                communicationAddressEntity.setMobile(council.getMobileNumber() != null ? council.getMobileNumber() : null);
                communicationAddressEntity.setAddressLine1(communicationAddress.getAddressLine1() != null ? communicationAddress.getAddressLine1() : null);
                communicationAddressEntity.setEmail(council.getEmail() != null ? council.getEmail() : null);
                communicationAddressEntity.setHouse(null);
                communicationAddressEntity.setStreet(null);
                communicationAddressEntity.setLocality(null);
                communicationAddressEntity.setLandmark(null);
                communicationAddressEntity.setHpProfileId(hpProfile.getId());
                communicationAddressEntity.setAddressTypeId(new in.gov.abdm.nmr.entity.AddressType(AddressType.COMMUNICATION.getId(), AddressType.COMMUNICATION.name()));
                communicationAddressEntity.setVillage(communicationAddress.getCity() != null ? villagesRepository.findByName(communicationAddress.getCity()) : null);
                communicationAddressEntity.setSubDistrict(communicationAddress.getSubDistricts() != null ? subDistrictRepository.findByName(communicationAddress.getSubDistricts()) : null);
                communicationAddressEntity.setState(communicationAddress.getState() != null ? stateRepository.findByName(communicationAddress.getState().toUpperCase()) : null);
                communicationAddressEntity.setDistrict(communicationAddress.getDistrict() != null ? districtRepository.findByDistrictNameAndStateId(communicationAddress.getDistrict().toUpperCase(), communicationAddressEntity.getState().getId()) : null);
                communicationAddressEntity.setCountry(communicationAddress.getCountry() != null ? countryRepository.findByName(communicationAddress.getCountry()) : null);

                iAddressRepository.saveAll(List.of(kycAddress, communicationAddressEntity));
            }else{
                iAddressRepository.saveAll(List.of(kycAddress));
            }

        }else{
            iAddressRepository.saveAll(List.of(kycAddress));
        }


    }

    @Override
    public void updateHealthProfessionalEmailMobile(BigInteger hpProfileId, HealthProfessionalPersonalRequestTo request) throws OtpException, InvalidRequestException {

        String transactionId = request.getTransactionId();
        if (request.getMobileNumber() != null) {
            if (transactionId == null) {
                throw new InvalidRequestException(MISSING_TRANSACTION_ID_ERROR);
            } else {
                if (otpService.isOtpVerified(transactionId)) {
                    throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage(),
                            HttpStatus.UNAUTHORIZED.toString());
                }
            }
        }

        if (request.getMobileNumber() != null) {
            iHpProfileRepository.updateHpProfileMobile(hpProfileId, request.getMobileNumber());
        }

        String eSignTransactionId = request.getESignTransactionId();
        if (eSignTransactionId != null && !eSignTransactionId.isBlank()) {
            HpProfile hpProfile = iHpProfileRepository.findHpProfileById(hpProfileId);
            if (hpProfile != null) {
                hpProfile.setTransactionId(eSignTransactionId);
                iHpProfileRepository.save(hpProfile);
            }

        }

        BigInteger masterHpProfileId = iHpProfileRepository.findMasterHpProfileByHpProfileId(hpProfileId);
        if (masterHpProfileId != null) {
            if (request.getMobileNumber() != null) {// update mobile_number hp_profile_master by hp_profile_master.id
                iHpProfileMasterRepository.updateMasterHpProfileMobile(masterHpProfileId, request.getMobileNumber());
            }
            if (eSignTransactionId != null && !eSignTransactionId.isBlank()) {
                HpProfileMaster hpProfileMaster = iHpProfileMasterRepository.findHpProfileMasterById(masterHpProfileId);
                if (hpProfileMaster != null) {
                    hpProfileMaster.setTransactionId(eSignTransactionId);
                    iHpProfileMasterRepository.save(hpProfileMaster);
                }

            }
        }
    }

    /**
     * Creates new unique token for reset password transaction
     *
     * @param verifyEmailLinkTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo getEmailVerificationLink(BigInteger hpProfileId, VerifyEmailLinkTo verifyEmailLinkTo) {

        try {

            HpProfile hpProfile = iHpProfileRepository.findHpProfileById(hpProfileId);

            if (hpProfile != null) {

                if (!(hpProfile.getUser().isEmailVerified() && hpProfile.getUser().getEmail().equals(verifyEmailLinkTo.getEmail()))) {

                    if (!userDaoService.checkEmailUsedByOtherUser(hpProfile.getUser().getId(), verifyEmailLinkTo.getEmail())) {

                        hpProfile.setEmailId(verifyEmailLinkTo.getEmail());
                        User user = userDaoService.findById(hpProfile.getUser().getId());
                        user.setEmail(verifyEmailLinkTo.getEmail());
                        user.setEmailVerified(false);
                        userDaoService.save(user);
                        iHpProfileRepository.save(hpProfile);
                        return notificationService.sendNotificationForEmailVerificationLink(verifyEmailLinkTo.getEmail(), generateLink(new SendLinkOnMailTo(verifyEmailLinkTo.getEmail())));
                    } else {

                        return new ResponseMessageTo(NMRConstants.EMAIL_USED_BY_OTHER_USER);
                    }
                } else {
                    return new ResponseMessageTo(NMRConstants.EMAIL_ALREADY_VERIFIED);
                }
            } else {
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }


    @Override
    public String generateLink(SendLinkOnMailTo sendLinkOnMailTo) {


        String token = RandomString.make(30);
        ResetToken resetToken = resetTokenRepository.findByUserName(sendLinkOnMailTo.getEmail());

        if (resetToken != null) {
            resetToken.setToken(token);
        } else {
            resetToken = new ResetToken(token, sendLinkOnMailTo.getEmail());
        }
        resetTokenRepository.save(resetToken);

        String resetPasswordLink = emailVerifyUrl + "/" + token;

        return resetPasswordLink;
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
        if (new Soundex().soundex(s1).equals(new Soundex().soundex(s2))) {
            log.info("Matched using soundex algorithm");
            return 100;
        }
        if (new Metaphone().isMetaphoneEqual(s1, s2)) {
            log.info("Matched using metaphone algorithm");
            return 100;
        }
        int maxLength = s2.length() > s1.length() ? s2.length() : s1.length();
        double levenshteinDistancePercentage = 100 - ((new LevenshteinDistance().apply(s1, s2) / (double) maxLength) * 100);
        if (levenshteinDistancePercentage >= NMRConstants.FUZZY_MATCH_LIMIT) {
            log.info("Matched using LevenshteinDistance algorithm");
            return levenshteinDistancePercentage;
        }
        int smiliarText = (similar(s1.toLowerCase(), s2.toLowerCase()) * 200) / (s1.length() + s2.length());
        if (smiliarText >= NMRConstants.FUZZY_MATCH_LIMIT) {
            log.info("Matched using similiar Text algorithm");
            return smiliarText;
        }
        return 0.0;
    }

    private int similar(String first, String second) {
        int p, q, l, sum;
        int pos1 = 0;
        int pos2 = 0;
        int max = 0;
        char[] arr1 = first.toCharArray();
        char[] arr2 = second.toCharArray();
        int firstLength = arr1.length;
        int secondLength = arr2.length;

        for (p = 0; p < firstLength; p++) {
            for (q = 0; q < secondLength; q++) {
                for (l = 0; (p + l < firstLength) && (q + l < secondLength) && (arr1[p + l] == arr2[q + l]); l++) ;
                if (l > max) {
                    max = l;
                    pos1 = p;
                    pos2 = q;
                }

            }
        }
        sum = max;
        if (sum > 0) {
            if (pos1 > 0 && pos2 > 0) {
                sum += this.similar(first.substring(0, pos1 > firstLength ? firstLength : pos1), second.substring(0, pos2 > secondLength ? secondLength : pos2));
            }

            if ((pos1 + max < firstLength) && (pos2 + max < secondLength)) {
                sum += this.similar(first.substring(pos1 + max, firstLength), second.substring(pos2 + max, secondLength));
            }
        }
        return sum;
    }
}
