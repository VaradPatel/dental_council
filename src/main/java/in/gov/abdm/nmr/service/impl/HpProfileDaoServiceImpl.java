package in.gov.abdm.nmr.service.impl;

import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;
import static in.gov.abdm.nmr.util.NMRUtil.coalesce;
import static in.gov.abdm.nmr.util.NMRUtil.isFileTypeSupported;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import in.gov.abdm.nmr.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.gov.abdm.nmr.dto.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.dto.HpPersonalUpdateRequestTO;
import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.HpRegistrationUpdateRequestTO;
import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import in.gov.abdm.nmr.dto.HpWorkProfileUpdateRequestTO;
import in.gov.abdm.nmr.dto.QualificationDetailRequestTO;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.AddressType;
import in.gov.abdm.nmr.entity.BroadSpeciality;
import in.gov.abdm.nmr.entity.Country;
import in.gov.abdm.nmr.entity.District;
import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.entity.HpNbeDetails;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.Language;
import in.gov.abdm.nmr.entity.LanguagesKnown;
import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.entity.State;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.SubDistrict;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.Villages;
import in.gov.abdm.nmr.entity.WorkProfile;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.repository.CountryRepository;
import in.gov.abdm.nmr.repository.CourseRepository;
import in.gov.abdm.nmr.repository.DistrictRepository;
import in.gov.abdm.nmr.repository.HpNbeDetailsRepository;
import in.gov.abdm.nmr.repository.IAddressRepository;
import in.gov.abdm.nmr.repository.ICollegeMasterRepository;
import in.gov.abdm.nmr.repository.IForeignQualificationDetailRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IQualificationDetailRepository;
import in.gov.abdm.nmr.repository.IRegistrationDetailRepository;
import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.repository.IStateRepository;
import in.gov.abdm.nmr.repository.LanguagesKnownRepository;
import in.gov.abdm.nmr.repository.SubDistrictRepository;
import in.gov.abdm.nmr.repository.UniversityMasterRepository;
import in.gov.abdm.nmr.repository.VillagesRepository;
import in.gov.abdm.nmr.repository.WorkNatureRepository;
import in.gov.abdm.nmr.repository.WorkProfileRepository;
import in.gov.abdm.nmr.repository.WorkStatusRepository;
import in.gov.abdm.nmr.service.ICouncilService;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.util.AuditLogPublisher;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import in.gov.abdm.nmr.util.XSSFileDetection;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HpProfileDaoServiceImpl implements IHpProfileDaoService {
    public static final String EXCEPTION_OCCURRED_WHILE_SAVING_ATTACHMENT = "Exception occurred while saving attachment {}.";
    @Autowired
    IHpProfileRepository iHpProfileRepository;
    @Autowired
    IAddressRepository iAddressRepository;
    @Autowired
    IRegistrationDetailRepository registrationDetailRepository;
    @Autowired
    IQualificationDetailRepository qualificationDetailRepository;
    @Autowired
    WorkProfileRepository workProfileRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    IStateRepository stateRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    IRegistrationDetailRepository iRegistrationDetailRepository;
    @Autowired
    IStateMedicalCouncilRepository iStateMedicalCouncilRepository;
    @Autowired
    WorkNatureRepository workNatureRepository;
    @Autowired
    WorkStatusRepository workStatusRepository;
    @Autowired
    SubDistrictRepository subDistrictRepository;
    @Autowired
    VillagesRepository villagesRepository;
    @Autowired
    IForeignQualificationDetailRepository iCustomQualificationDetailRepository;
    @Autowired
    HpNbeDetailsRepository hpNbeDetailsRepository;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ICollegeMasterRepository collegeMasterRepository;
    @Autowired
    UniversityMasterRepository universityMasterRepository;
    @Autowired
    ICouncilService councilService;
    @Autowired
    IStateMedicalCouncilRepository stateMedicalCouncilRepository;
    @Autowired
    LanguagesKnownRepository languagesKnownRepository;
    @Autowired
    AuditLogPublisher auditLogPublisher;

    /**
     * Info: this method will fetch health professional registration details from the IMR data source. if details
     * are found then they are validated against the NMR database for verifying user existence.
     * if details are not found in the IMR data source then they are checked in the NMR database.
     *
     * @param councilId
     * @param registrationNumber
     * @return
     * @throws NoDataFoundException
     */
    public HpSmcDetailTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber) throws NoDataFoundException {
        HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();

        StateMedicalCouncil stateMedicalCouncil =
                stateMedicalCouncilRepository.findStateMedicalCouncilById(BigInteger.valueOf(councilId));

        String councilName = stateMedicalCouncil.getName();
        List<Council> councils = councilService.getCouncilByRegistrationNumberAndCouncilName(registrationNumber, councilName);
        RegistrationDetails registrationDetails = iRegistrationDetailRepository.getRegistrationDetailsByRegistrationNoAndStateMedicalCouncilId(registrationNumber, BigInteger.valueOf(councilId));

        if (!councils.isEmpty()) {
            Council council = councils.get(0);
            hpSmcDetailTO.setHpName(council.getFullName());
            hpSmcDetailTO.setCouncilName(council.getRegistrationsDetails().get(0).getCouncilName());
            hpSmcDetailTO.setRegistrationNumber(council.getRegistrationsDetails().get(0).getRegistrationNo());
            hpSmcDetailTO.setEmailId(council.getEmail());
            hpSmcDetailTO.setAlreadyRegisteredInNmr(registrationDetails != null);
        } else if (registrationDetails != null) {
            hpSmcDetailTO.setHpName(registrationDetails.getHpProfileId().getFullName());
            hpSmcDetailTO.setCouncilName(registrationDetails.getStateMedicalCouncil().getName());
            hpSmcDetailTO.setRegistrationNumber(registrationDetails.getRegistrationNo());
            hpSmcDetailTO.setEmailId(registrationDetails.getHpProfileId().getEmailId());
            hpSmcDetailTO.setAlreadyRegisteredInNmr(true);
        } else {
            throw new NoDataFoundException();
        }
        return hpSmcDetailTO;

    }

    @Override
    public HpProfileUpdateResponseTO updateHpPersonalDetails(BigInteger hpProfileId, HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException {

        log.info("In HpProfileDaoServiceImpl : updateHpPersonalDetails method");

        HpProfile hpProfile = iHpProfileRepository.findById(NMRUtil.coalesce(hpProfileId, BigInteger.ZERO)).orElse(null);

        if (hpProfile == null) {
            log.debug("Initiating HP Profile Insertion flow since there are no existing HP Profiles with this hp_profile_id or The existing HP Profile is now Approved");
            hpProfile = new HpProfile();
        }

        mapHpPersonalRequestToEntity(hpPersonalUpdateRequestTO, hpProfile);
        iHpProfileRepository.save(hpProfile);

        if (hpPersonalUpdateRequestTO.getCommunicationAddress() != null) {
            Address address = NMRUtil.coalesce(iAddressRepository.getCommunicationAddressByHpProfileId(hpProfile.getId(), in.gov.abdm.nmr.enums.AddressType.COMMUNICATION.getId()), new Address());
            mapAddressRequestToEntity(hpProfileId, hpPersonalUpdateRequestTO, address);
            iAddressRepository.save(address);
        }

        auditLogPublisher.publishHpProfileAuditLog(hpProfile);

        log.info("HpProfileDaoServiceImpl : updateHpPersonalDetails method : Execution Successful. ");
        return new HpProfileUpdateResponseTO(204, "Record Added/Updated Successfully!", hpProfileId);
    }

    @SneakyThrows
    @Override
    public HpProfileUpdateResponseTO updateHpRegistrationDetails(BigInteger hpProfileId,
                                                                 HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, MultipartFile registrationCertificate, List<MultipartFile> degreeCertificate) throws NmrException, InvalidRequestException {

        log.info("In HpProfileDaoServiceImpl : updateHpRegistrationDetails method");
        isFileTypeSupported(registrationCertificate);
        if(XSSFileDetection.isMaliciousCodeInFile(registrationCertificate)) {
			throw new InvalidFileUploadException();
        }
        
        for (MultipartFile file : degreeCertificate) {
            isFileTypeSupported(file);
            if(XSSFileDetection.isMaliciousCodeInFile(file)) {
    			throw new InvalidFileUploadException();
            }
        }
       

        if (hpRegistrationUpdateRequestTO.getRegistrationDetail() != null) {
            hpRegistrationUpdateRequestTO.getRegistrationDetail().setFileName(registrationCertificate != null ? registrationCertificate.getOriginalFilename() : null);
        }
        RegistrationDetails registrationDetail = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);

        HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElseThrow(InvalidRequestException::new);

        if (registrationDetail == null) {
            log.debug("Initiating Registration details Insertion flow since there were no matching Registration details found for the given hp_profile_id. ");
            registrationDetail = new RegistrationDetails();
            mapRegistrationRequestToEntity(hpRegistrationUpdateRequestTO, registrationDetail, hpProfile, registrationCertificate);
            registrationDetailRepository.save(registrationDetail);
        } else {
            log.debug("Initiating Registration details Updation flow since there was an existing Registration detail for the given hp_profile_id. ");
            mapRegistrationRequestToEntity(hpRegistrationUpdateRequestTO, registrationDetail, hpProfile, registrationCertificate);
        }
        if (hpRegistrationUpdateRequestTO.getQualificationDetails() != null && !hpRegistrationUpdateRequestTO.getQualificationDetails().isEmpty()) {
            if (1 == hpRegistrationUpdateRequestTO.getQualificationDetails().size() && hpProfile.getUser() != null) {
                if (NMRConstants.INDIA.equals(hpRegistrationUpdateRequestTO.getQualificationDetails().get(0).getQualificationFrom())) {
                    iCustomQualificationDetailRepository.deleteInternationalQualificationByUserId(hpProfile.getUser().getId());
                } else if (NMRConstants.INTERNATIONAL.equals(hpRegistrationUpdateRequestTO.getQualificationDetails().get(0).getQualificationFrom())) {
                    qualificationDetailRepository.deleteIndianQualificationByUserId(hpProfile.getUser().getId());
                }
            }
            log.debug("Saving Qualification Details");
            saveQualificationDetailsForRegistration(hpProfile, registrationDetail, hpRegistrationUpdateRequestTO.getQualificationDetails(), degreeCertificate);
        }
        if (NMRConstants.INTERNATIONAL.equals(hpRegistrationUpdateRequestTO.getQualificationDetails().get(0).getQualificationFrom())) {
            HpNbeDetails hpNbeDetails = hpNbeDetailsRepository.findByUserId(hpProfile.getUser().getId());
            if (hpNbeDetails == null) {
                log.debug("Initiating NBE details Insertion flow since there were no matching NBE details found for the given hp_profile_id. ");
                hpNbeDetails = new HpNbeDetails();
                mapNbeRequestDetailsToEntity(hpRegistrationUpdateRequestTO, hpNbeDetails, hpProfile);
                hpNbeDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            } else {
                log.debug("Initiating NBE details Updation flow since there was an existing NBE detail for the given hp_profile_id. ");
                mapNbeRequestDetailsToEntity(hpRegistrationUpdateRequestTO, hpNbeDetails, hpProfile);
            }
            hpNbeDetailsRepository.save(hpNbeDetails);
        }
            hpProfile.setRegistrationId(hpRegistrationUpdateRequestTO.getRegistrationDetail().getRegistrationNumber());
            iHpProfileRepository.save(hpProfile);
        log.info("HpProfileDaoServiceImpl : updateHpRegistrationDetails method : Execution Successful. ");

        return new HpProfileUpdateResponseTO(204,
                "Registration Added/Updated Successfully!!", hpProfileId);
    }

    @Override
    public HpProfileUpdateResponseTO updateWorkProfileDetails(BigInteger hpProfileId,
                                                              HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<MultipartFile> proofs) throws InvalidRequestException, NmrException, NotFoundException {

        log.info("In HpProfileDaoServiceImpl : updateWorkProfileDetails method");

        if (proofs != null && proofs.size() > 1) {
            log.debug("Initiating Addition of Proofs since Proofs are provided as a part of input payload. ");
            hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().stream().forEach(currentWorkDetailsTO -> {
                MultipartFile file = proofs.get(hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().indexOf(currentWorkDetailsTO));
                try {
                    currentWorkDetailsTO.setProof(file != null ? file.getBytes() : null);
                } catch (IOException e) {
                    log.error(EXCEPTION_OCCURRED_WHILE_SAVING_ATTACHMENT, e);
                }
            });
            log.debug("Addition of proofs is successful");
        }
        List<WorkProfile> workProfile = new ArrayList<>();
        BigInteger userId = null;
        Optional<HpProfile> hpProfileOptional = iHpProfileRepository.findById(hpProfileId);
        if (hpProfileOptional.isPresent()) {
            User user = hpProfileOptional.get().getUser();
            if (user != null) {
                userId = user.getId();
                if(hpWorkProfileUpdateRequestTO.getWorkDetails().getIsUserCurrentlyWorking().toString().equals(NMRConstants.DOCTOR_CURRENTLY_NOT_WORKING)){
                    workProfileRepository.markAsDeleteByHpUserId(userId);
                    languagesKnownRepository.deleteAllByUserId(userId);
                }
                workProfile = workProfileRepository.getActiveWorkProfileDetailsByUserId(userId);
            } else {
                throw new NotFoundException(NMRError.NO_SUCH_ELEMENT.getCode(), NMRError.NO_SUCH_ELEMENT.getMessage());
            }
        }
        if (workProfile.isEmpty()) {
            log.debug("Initiation of Work Profile Insertion flow for Work Profile details since there are no matching Work Profiles found for the provided hp_profile_id");
            workProfile = new ArrayList<>();
            mapWorkRequestToEntity(hpWorkProfileUpdateRequestTO, workProfile, hpProfileId, userId);
        } else {
            log.debug("Initiation of Work Profile Updation flow for Work Profile details since there were matching Work Profiles found for the provided hp_profile_id");
            mapWorkRequestToEntity(hpWorkProfileUpdateRequestTO, workProfile, hpProfileId, userId);
        }

        saveKnownLanguages(hpWorkProfileUpdateRequestTO, userId);

        log.info("HpProfileDaoServiceImpl : updateWorkProfileDetails method : Execution Successful. ");

        return new HpProfileUpdateResponseTO(204,
                "Registration Added/Updated Successfully!!", hpProfileId);
    }

    private void saveKnownLanguages(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, BigInteger userId) {
        languagesKnownRepository.deleteAllByUserId(userId);
        List<BigInteger> languagesKnownIds = hpWorkProfileUpdateRequestTO.getLanguagesKnownIds();
        List<BigInteger> languagesKnownEarlierIds = new ArrayList<>();
        if (languagesKnownIds != null && !languagesKnownIds.isEmpty()) {
            List<LanguagesKnown> languagesKnownEarlier = languagesKnownRepository.findByUserId(userId);
            if (languagesKnownEarlier != null && !languagesKnownEarlier.isEmpty())
                languagesKnownEarlier.forEach(languagesKnown -> languagesKnownEarlierIds.add(languagesKnown.getLanguage().getId()));
        }
        List<LanguagesKnown> languagesKnownLater = new ArrayList<>();
        BigInteger tempUserId = userId;
        if (languagesKnownIds != null && !languagesKnownEarlierIds.isEmpty()) {
            languagesKnownEarlierIds.forEach(languagesKnownIds::remove);
        }
        if (languagesKnownIds != null && !languagesKnownIds.isEmpty()) {
            languagesKnownIds.forEach(languagesKnown -> {
                LanguagesKnown languagesKnownObject = new LanguagesKnown();
                languagesKnownObject.setLanguage(entityManager.getReference(Language.class, languagesKnown));
                languagesKnownObject.setUser(entityManager.getReference(User.class, tempUserId));
                languagesKnownLater.add(languagesKnownObject);
            });
        }
        languagesKnownRepository.saveAll(languagesKnownLater);
    }

    @Override
    public void saveQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, List<QualificationDetailRequestTO> qualificationDetailRequestTOS, List<MultipartFile> proofs) throws InvalidRequestException {
        if (qualificationDetailRequestTOS != null) {
            saveIndianQualificationDetails(hpProfile, newRegistrationDetails, qualificationDetailRequestTOS.stream().filter(qualificationDetailRequestTO -> NMRConstants.INDIA.equals(qualificationDetailRequestTO.getQualificationFrom())).toList(), proofs);
            saveInternationalQualificationDetails(hpProfile, newRegistrationDetails, qualificationDetailRequestTOS.stream().filter(qualificationDetailRequestTO -> NMRConstants.INTERNATIONAL.equals(qualificationDetailRequestTO.getQualificationFrom())).toList(), proofs);
        }
    }

    public void saveQualificationDetailsForRegistration(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, List<QualificationDetailRequestTO> qualificationDetailRequestTOS, List<MultipartFile> proofs) throws InvalidRequestException {
        if (qualificationDetailRequestTOS != null) {
            saveIndianQualificationDetailsForRegistration(hpProfile, newRegistrationDetails, qualificationDetailRequestTOS.stream().filter(qualificationDetailRequestTO -> NMRConstants.INDIA.equals(qualificationDetailRequestTO.getQualificationFrom())).toList(), proofs);
            saveInternationalQualificationDetailsForRegistration(hpProfile, newRegistrationDetails, qualificationDetailRequestTOS.stream().filter(qualificationDetailRequestTO -> NMRConstants.INTERNATIONAL.equals(qualificationDetailRequestTO.getQualificationFrom())).toList(), proofs);
        }
    }

    @Override
    public HpProfile findLatestEntryByUserid(BigInteger userId) {
        return iHpProfileRepository.findLatestEntryByUserid(userId);

    }

    @Override
    public HpProfilePictureResponseTO uploadHpProfilePhoto(MultipartFile file, BigInteger hpProfileId)
            throws IOException, InvalidRequestException, InvalidFileUploadException {

        HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);
        if (hpProfile == null) {
            throw new InvalidRequestException(NMRError.INVALID_REQUEST.getCode(), NMRError.INVALID_REQUEST.getMessage());
        }
        isFileTypeSupported(file);
        if(XSSFileDetection.isMaliciousCodeInFile(file)) {
			throw new InvalidFileUploadException();
        }

        String encodedPhoto = Base64.getEncoder().encodeToString(file.getBytes());
        hpProfile.setProfilePhoto(encodedPhoto);
        hpProfile.setPicName(file.getOriginalFilename());
        HpProfile insertedData = iHpProfileRepository.save(hpProfile);
            HpProfilePictureResponseTO hpProfilePictureResponseTO = new HpProfilePictureResponseTO();
        hpProfilePictureResponseTO.setProfilePicture(insertedData.getProfilePhoto());
        hpProfilePictureResponseTO.setPicName(insertedData.getPicName());
        hpProfilePictureResponseTO.setMessage(SUCCESS_RESPONSE);
        hpProfilePictureResponseTO.setStatus(200);
        return hpProfilePictureResponseTO;
    }


    @Override
    public HpProfile findById(BigInteger id) {
        return iHpProfileRepository.findById(id).orElse(null);
    }

    private void saveIndianQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
                                                List<QualificationDetailRequestTO> qualificationDetailRequestTOS, List<MultipartFile> proofs) throws InvalidRequestException {

        List<QualificationDetails> qualificationDetails = new ArrayList<>();
        for (QualificationDetailRequestTO indianQualification : qualificationDetailRequestTOS) {
            QualificationDetails qualification = new QualificationDetails();
            if (indianQualification.getId() != null) {
                qualification = qualificationDetailRepository.findById(indianQualification.getId()).orElseThrow(InvalidRequestException::new);
                mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification, (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(indianQualification)) : null);
            } else {
                mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification,  (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(indianQualification)) : null);
                qualificationDetails.add(qualification);
            }
        }
        qualificationDetailRepository.saveAll(qualificationDetails);

    }

    private void saveIndianQualificationDetailsForRegistration(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
                                                               List<QualificationDetailRequestTO> qualificationDetailRequestTOS, List<MultipartFile> proofs) throws InvalidRequestException {

        List<QualificationDetails> qualificationDetails = new ArrayList<>();
        for (QualificationDetailRequestTO indianQualification : qualificationDetailRequestTOS) {
            QualificationDetails qualification = qualificationDetailRepository.getBasicQualificationDetailsByUserId(hpProfile.getUser().getId());
            if (qualification!= null) {
                mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification, (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(indianQualification)) : null);
            } else {
                qualification=new QualificationDetails();
                mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification,  (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(indianQualification)) : null);
                qualificationDetails.add(qualification);
            }
        }
        qualificationDetailRepository.saveAll(qualificationDetails);

    }
    private void mapIndianQualificationRequestToEntity(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, QualificationDetailRequestTO indianQualification, QualificationDetails qualification, MultipartFile proof) throws InvalidRequestException {

        qualification.setCountry(countryRepository.findById(indianQualification.getCountry().getId()).orElseThrow(InvalidRequestException::new));
        qualification.setState(stateRepository.findById(indianQualification.getState().getId()).orElseThrow(InvalidRequestException::new));
        qualification.setCollege(collegeMasterRepository.findCollegeMasterById(indianQualification.getCollege().getId()));
        qualification.setUniversity(universityMasterRepository.findUniversityMasterById(indianQualification.getUniversity().getId()));
        qualification.setCourse(courseRepository.findById(indianQualification.getCourse().getId()).orElseThrow(InvalidRequestException::new));
        if(qualification.getIsVerified()==null) {
            qualification.setIsVerified(NMRConstants.QUALIFICATION_STATUS_PENDING);
        }
        qualification.setQualificationYear(indianQualification.getQualificationYear());
        qualification.setQualificationMonth(indianQualification.getQualificationMonth());
        qualification.setIsNameChange(indianQualification.getIsNameChange());
        qualification.setRegistrationDetails(newRegistrationDetails);
        qualification.setHpProfile(hpProfile);
        qualification.setRequestId(
                coalesce(indianQualification.getRequestId(), hpProfile.getRequestId()));
        qualification.setBroadSpeciality(indianQualification.getBroadSpecialityId() != null ? BroadSpeciality.builder().id(indianQualification.getBroadSpecialityId()).build() : null);
        qualification.setSuperSpecialityName(indianQualification.getSuperSpecialityName());
        qualification.setUser(hpProfile.getUser());
        qualification.setStateMedicalCouncil(indianQualification.getStateMedicalCouncil());
        qualification.setRegistrationNumber(indianQualification.getRegistrationNumber());
        qualification.setRegistrationDate(indianQualification.getRegistrationDate());
        if (proof != null && !proof.isEmpty()) {
            qualification.setFileName(proof.getOriginalFilename());
            try {
                qualification.setCertificate(proof.getBytes());
            } catch (IOException e) {
                log.error(EXCEPTION_OCCURRED_WHILE_SAVING_ATTACHMENT, e);
            }
        }
    }


    private void saveInternationalQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
                                                       List<QualificationDetailRequestTO> qualificationDetailRequestTOS, List<MultipartFile> proofs) throws InvalidRequestException {
        List<ForeignQualificationDetails> internationQualifications = new ArrayList<>();
        for (QualificationDetailRequestTO internationalQualification : qualificationDetailRequestTOS) {
            ForeignQualificationDetails customQualification = new ForeignQualificationDetails();
            if (internationalQualification.getId() != null) {
                customQualification = iCustomQualificationDetailRepository.findById(internationalQualification.getId()).orElseThrow(InvalidRequestException::new);
                mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationalQualification, customQualification,  (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(internationalQualification)) : null);
            } else {
                mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationalQualification, customQualification,  (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(internationalQualification)) : null);
                internationQualifications.add(customQualification);
            }
        }
        iCustomQualificationDetailRepository.saveAll(internationQualifications);
    }

    private void saveInternationalQualificationDetailsForRegistration(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
                                                                      List<QualificationDetailRequestTO> qualificationDetailRequestTOS, List<MultipartFile> proofs) throws InvalidRequestException {
        List<ForeignQualificationDetails> internationQualifications = new ArrayList<>();
        for (QualificationDetailRequestTO internationalQualification : qualificationDetailRequestTOS) {
            ForeignQualificationDetails customQualification = iCustomQualificationDetailRepository.getBasicQualificationDetailsByUserId(hpProfile.getUser().getId());
            if (customQualification != null) {
                mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationalQualification, customQualification,  (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(internationalQualification)) : null);
            } else {
                customQualification = new ForeignQualificationDetails();
                mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationalQualification, customQualification,  (proofs != null && !proofs.isEmpty()) ? proofs.get(qualificationDetailRequestTOS.indexOf(internationalQualification)) : null);
                internationQualifications.add(customQualification);
            }
        }
        iCustomQualificationDetailRepository.saveAll(internationQualifications);
    }

    private void mapQualificationRequestToEntity(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, QualificationDetailRequestTO newCustomQualification, ForeignQualificationDetails customQualification, MultipartFile proof) throws InvalidRequestException {
        customQualification.setCountry(newCustomQualification.getCountry().getName());
        customQualification.setState(newCustomQualification.getState() != null ? newCustomQualification.getState().getName() : null);
        customQualification.setCollege(newCustomQualification.getCollege().getName());
        customQualification.setUniversity(newCustomQualification.getUniversity() != null ? newCustomQualification.getUniversity().getName() : null);
        String courseName = newCustomQualification.getCourse().getCourseName();
        customQualification.setCourse(courseName != null ? courseName : newCustomQualification.getCourse().getName());
        if(customQualification.getIsVerified()==null) {
            customQualification.setIsVerified(NMRConstants.QUALIFICATION_STATUS_PENDING);
        }
        customQualification.setQualificationYear(newCustomQualification.getQualificationYear());
        customQualification.setQualificationMonth(newCustomQualification.getQualificationMonth());
        customQualification.setIsNameChange(newCustomQualification.getIsNameChange());
        customQualification.setRegistrationDetails(newRegistrationDetails);
        customQualification.setRequestId(
                coalesce(newCustomQualification.getRequestId(), hpProfile.getRequestId()));
        customQualification.setHpProfile(hpProfile);
        customQualification.setBroadSpeciality(newCustomQualification.getBroadSpecialityId() != null ? BroadSpeciality.builder().id(newCustomQualification.getBroadSpecialityId()).build() : null);
        customQualification.setSuperSpecialityName(newCustomQualification.getSuperSpecialityName());
        customQualification.setUser(hpProfile.getUser());
        if (proof != null && !proof.isEmpty()) {
            customQualification.setFileName(proof.getOriginalFilename());
            try {
                customQualification.setCertificate(proof.getBytes());
            } catch (IOException e) {
                log.error("Exception occurred while saving attachment.", e);
            }
        }
    }

    private void mapAddressRequestToEntity(BigInteger hpProfileId, HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO, Address addressData) {
        addressData.setAddressLine1(hpPersonalUpdateRequestTO.getCommunicationAddress().getAddressLine1());
        addressData.setPincode(hpPersonalUpdateRequestTO.getCommunicationAddress().getPincode());
        addressData.setEmail(hpPersonalUpdateRequestTO.getPersonalDetails().getEmail());
        addressData.setMobile(hpPersonalUpdateRequestTO.getPersonalDetails().getMobile());

        Country communicationCountry = countryRepository
                .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getCountry().getId())
                .orElse(null);
        addressData.setCountry(communicationCountry);

        State communicationState = stateRepository
                .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getState().getId()).orElse(null);
        addressData.setState(communicationState);

        if (hpPersonalUpdateRequestTO.getCommunicationAddress().getDistrict() != null) {
            District communicationDistrict = districtRepository
                    .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getDistrict().getId())
                    .orElse(null);
            addressData.setDistrict(communicationDistrict);
        }

        if (hpPersonalUpdateRequestTO.getCommunicationAddress().getSubDistrict() != null) {
            SubDistrict communicationSubDistrict = subDistrictRepository
                    .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getSubDistrict().getId())
                    .orElse(null);
            addressData.setSubDistrict(communicationSubDistrict);
        }

        if (hpPersonalUpdateRequestTO.getCommunicationAddress().getVillage() != null) {
            Villages communicationCity = villagesRepository
                    .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getVillage().getId())
                    .orElse(null);
            addressData.setVillage(communicationCity);
        }

        AddressType addressType = new AddressType();
        addressType.setId(hpPersonalUpdateRequestTO.getCommunicationAddress().getAddressType() != null ? hpPersonalUpdateRequestTO.getCommunicationAddress().getAddressType().getId() : in.gov.abdm.nmr.enums.AddressType.COMMUNICATION.getId());
        addressData.setAddressTypeId(addressType);
        addressData.setHpProfileId(hpProfileId);

        addressData.setHouse(hpPersonalUpdateRequestTO.getCommunicationAddress().getHouse());
        addressData.setStreet(hpPersonalUpdateRequestTO.getCommunicationAddress().getStreet());
        addressData.setLandmark(hpPersonalUpdateRequestTO.getCommunicationAddress().getLandmark());
        addressData.setLocality(hpPersonalUpdateRequestTO.getCommunicationAddress().getLocality());
    }

    private void mapHpPersonalRequestToEntity(HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO, HpProfile hpProfile) {
        hpProfile.setSalutation(hpPersonalUpdateRequestTO.getPersonalDetails().getSalutation());
        hpProfile.setAadhaarToken(hpPersonalUpdateRequestTO.getPersonalDetails().getAadhaarToken());
        hpProfile.setFirstName(hpPersonalUpdateRequestTO.getPersonalDetails().getFirstName());
        hpProfile.setMiddleName(hpPersonalUpdateRequestTO.getPersonalDetails().getMiddleName());
        hpProfile.setLastName(hpPersonalUpdateRequestTO.getPersonalDetails().getLastName());
        hpProfile.setFatherName(hpPersonalUpdateRequestTO.getPersonalDetails().getFatherName());
        hpProfile.setMotherName(hpPersonalUpdateRequestTO.getPersonalDetails().getMotherName());
        hpProfile.setSpouseName(hpPersonalUpdateRequestTO.getPersonalDetails().getSpouseName());
        hpProfile.setGender(hpPersonalUpdateRequestTO.getPersonalDetails().getGender());
        hpProfile.setDateOfBirth(hpPersonalUpdateRequestTO.getPersonalDetails().getDateOfBirth());
        if (hpPersonalUpdateRequestTO.getRequestId() != null) {
            hpProfile.setRequestId(hpPersonalUpdateRequestTO.getRequestId());
        }
        hpProfile.setIsSameAddress(hpPersonalUpdateRequestTO.getCommunicationAddress().getIsSameAddress());
        Country countryNationality = countryRepository
                .findById(hpPersonalUpdateRequestTO.getPersonalDetails().getCountryNationality().getId())
                .orElse(null);
        hpProfile.setCountryNationality(countryNationality);
        hpProfile.setFullName(hpPersonalUpdateRequestTO.getPersonalDetails().getFullName());
        hpProfile.setEmailId(hpPersonalUpdateRequestTO.getPersonalDetails().getEmail());
    }

    private void mapNbeRequestDetailsToEntity(HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, HpNbeDetails hpNbeDetails, HpProfile hpProfile) {
        if (hpRegistrationUpdateRequestTO.getHpNbeDetails() != null) {
            hpNbeDetails.setMarksObtained(hpRegistrationUpdateRequestTO.getHpNbeDetails().getMarksObtained());
            hpNbeDetails.setMonthOfPassing(hpRegistrationUpdateRequestTO.getHpNbeDetails().getMonthOfPassing());
            hpNbeDetails.setRollNo(hpRegistrationUpdateRequestTO.getHpNbeDetails().getRollNo());
            hpNbeDetails.setUserResult(hpRegistrationUpdateRequestTO.getHpNbeDetails().getUserResult());
            hpNbeDetails.setYearOfPassing(hpRegistrationUpdateRequestTO.getHpNbeDetails().getYearOfPassing());
            hpNbeDetails.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            hpNbeDetails.setHpProfile(hpProfile);
            hpNbeDetails.setUser(hpProfile.getUser());
            hpNbeDetails.setPassportNumber(hpRegistrationUpdateRequestTO.getHpNbeDetails().getPassportNumber());
        }
    }

    @SneakyThrows
    private void mapRegistrationRequestToEntity(HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, RegistrationDetails registrationDetail, HpProfile hpProfile, MultipartFile registrationCertificate) {
        log.debug("In HpProfileDaoServiceImpl : mapRegistrationRequestToEntity method");
        if (hpRegistrationUpdateRequestTO.getRegistrationDetail() != null) {
            registrationDetail.setHpProfileId(hpProfile);
            registrationDetail.setRegistrationDate(hpRegistrationUpdateRequestTO.getRegistrationDetail().getRegistrationDate());
            registrationDetail.setRegistrationNo(hpRegistrationUpdateRequestTO.getRegistrationDetail().getRegistrationNumber());
            registrationDetail.setStateMedicalCouncil(iStateMedicalCouncilRepository
                    .findById(hpRegistrationUpdateRequestTO.getRegistrationDetail().getStateMedicalCouncil().getId()).orElseThrow(InvalidRequestException::new));
            String isRenewable = hpRegistrationUpdateRequestTO.getRegistrationDetail().getIsRenewable();
            registrationDetail.setIsRenewable(isRenewable);
            if (NMRConstants.RENEWABLE_REGISTRATION_CODE.equalsIgnoreCase(isRenewable)) {
                registrationDetail.setRenewableRegistrationDate(hpRegistrationUpdateRequestTO.getRegistrationDetail().getRenewableRegistrationDate());
            }
            registrationDetail.setIsNameChange(hpRegistrationUpdateRequestTO.getRegistrationDetail().getIsNameChange());
            if (registrationCertificate != null && !registrationCertificate.isEmpty()) {
                registrationDetail.setCertificate(registrationCertificate.getBytes());
                registrationDetail.setFileName(hpRegistrationUpdateRequestTO.getRegistrationDetail().getFileName());
            }
        }
        registrationDetail.setHpProfileId(hpProfile);
        registrationDetail.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetail.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    }


    @SneakyThrows
    private void mapWorkRequestToEntity(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<WorkProfile> existingWorkDetails, BigInteger hpProfileId, BigInteger userId) {
        if (!existingWorkDetails.isEmpty() && !hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().isEmpty()) {
            updateWorkProfileRecords(hpWorkProfileUpdateRequestTO, existingWorkDetails, hpProfileId, userId);
        } else {
            saveWorkProfileRecords(hpWorkProfileUpdateRequestTO, hpProfileId, userId);
        }
    }

    private WorkProfile workProfileObjectMapping(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, WorkProfile addWorkProfile, CurrentWorkDetailsTO currentWorkDetailsTO, BigInteger hpProfileId, BigInteger userId) {
        addWorkProfile.setRequestId(hpWorkProfileUpdateRequestTO.getRequestId());
        addWorkProfile.setHpProfileId(hpProfileId);
        addWorkProfile.setReason(hpWorkProfileUpdateRequestTO.getWorkDetails().getReason());
        addWorkProfile.setRemark(hpWorkProfileUpdateRequestTO.getWorkDetails().getRemark());
        if (hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkNature() != null) {
            addWorkProfile.setWorkNature(workNatureRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkNature().getId()).get());
        }
        if (hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkStatus() != null) {
            addWorkProfile.setWorkStatus(workStatusRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkStatus().getId()).get());
        }
        addWorkProfile.setExperienceInYears(hpWorkProfileUpdateRequestTO.getWorkDetails().getExperienceInYears());
        addWorkProfile.setIsUserCurrentlyWorking(hpWorkProfileUpdateRequestTO.getWorkDetails().getIsUserCurrentlyWorking());
        addWorkProfile.setFacilityId(currentWorkDetailsTO.getFacilityId());
        addWorkProfile.setFacilityTypeId(currentWorkDetailsTO.getFacilityTypeId());
        addWorkProfile.setUrl(currentWorkDetailsTO.getUrl());
        addWorkProfile
                .setWorkOrganization(currentWorkDetailsTO.getWorkOrganization());
        addWorkProfile.setOrganizationType(currentWorkDetailsTO.getOrganizationType());
        if (currentWorkDetailsTO.getAddress() != null) {
            addWorkProfile.setAddress(currentWorkDetailsTO.getAddress().getAddressLine1());
            addWorkProfile.setState(currentWorkDetailsTO.getAddress().getState().getId() != null ? stateRepository.findById(currentWorkDetailsTO.getAddress().getState().getId()).get() : null);
            addWorkProfile.setDistrict(currentWorkDetailsTO.getAddress().getDistrict().getId() != null ? districtRepository.findById(currentWorkDetailsTO.getAddress().getDistrict().getId()).get() : null);
            addWorkProfile.setPincode(currentWorkDetailsTO.getAddress().getPincode());
        }
        addWorkProfile.setProofOfWorkAttachment(currentWorkDetailsTO.getProof());
        addWorkProfile.setRegistrationNo(hpWorkProfileUpdateRequestTO.getRegistrationNo());
        addWorkProfile.setUserId(userId);
        addWorkProfile.setSystemOfMedicine(currentWorkDetailsTO.getSystemOfMedicine());
        addWorkProfile.setDesignation(currentWorkDetailsTO.getDesignation());
        addWorkProfile.setDepartment(currentWorkDetailsTO.getDepartment());
        return addWorkProfile;
    }

    private void updateWorkProfileRecords(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<WorkProfile> existingWorkDetails,
                                          BigInteger hpProfileId, BigInteger userId) {
        Set<String> existingFacilities = existingWorkDetails.stream().map(WorkProfile::getFacilityId).collect(Collectors.toSet());
        Set<CurrentWorkDetailsTO> newUniqueCurrentWorkDetails = hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().stream().filter(currentWorkDetailsTO1 -> !existingFacilities.contains(currentWorkDetailsTO1.getFacilityId())).collect(Collectors.toSet());
        List<WorkProfile> workProfileDetailsList = new ArrayList<>();
        if(hpWorkProfileUpdateRequestTO.getCurrentWorkDetails()!=null) {
            existingWorkDetails.forEach(addWorkProfile ->
                hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().forEach(currentWorkDetailsTO -> {
                    if (addWorkProfile.getFacilityId()!=null && addWorkProfile.getFacilityId().equals(currentWorkDetailsTO.getFacilityId())) {
                        workProfileDetailsList.add(workProfileObjectMapping(hpWorkProfileUpdateRequestTO, addWorkProfile, currentWorkDetailsTO, hpProfileId, userId));
                        workProfileRepository.saveAll(workProfileDetailsList);
                    }
                }));
        }
        if (newUniqueCurrentWorkDetails != null && !newUniqueCurrentWorkDetails.isEmpty()) {
            newUniqueCurrentWorkDetails.forEach(currentWorkDetailsTO -> {
                WorkProfile workProfile = new WorkProfile();
                workProfileDetailsList.add(workProfileObjectMapping(hpWorkProfileUpdateRequestTO, workProfile, currentWorkDetailsTO, hpProfileId, userId));
            });
            workProfileRepository.saveAll(workProfileDetailsList);
        }
    }

    private void saveWorkProfileRecords(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, BigInteger hpProfileId, BigInteger userId) {

        List<WorkProfile> workProfileDetailsList = new ArrayList<>();
        if(hpWorkProfileUpdateRequestTO.getWorkDetails().getIsUserCurrentlyWorking().toString().equals(NMRConstants.DOCTOR_CURRENTLY_NOT_WORKING) && (hpWorkProfileUpdateRequestTO.getCurrentWorkDetails()==null || hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().isEmpty())){
            WorkProfile workProfile=new WorkProfile();
            workProfile.setHpProfileId(hpProfileId);
            workProfile.setUserId(userId);
            workProfile.setIsUserCurrentlyWorking(Integer.valueOf(NMRConstants.DOCTOR_CURRENTLY_NOT_WORKING));
            workProfile.setRemark(hpWorkProfileUpdateRequestTO.getWorkDetails().getRemark());
            workProfile.setReason(hpWorkProfileUpdateRequestTO.getWorkDetails().getReason());
            workProfile.setExperienceInYears(hpWorkProfileUpdateRequestTO.getWorkDetails().getExperienceInYears());
            workProfileRepository.save(workProfile);
        }
        else {
            workProfileRepository.deleteCurrentlyNotWorkingByHpUserId(userId);
            if(hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().isEmpty()){

                WorkProfile workProfile=new WorkProfile();
                workProfile.setHpProfileId(hpProfileId);
                workProfile.setUserId(userId);
                workProfile.setIsUserCurrentlyWorking(Integer.valueOf(NMRConstants.DOCTOR_CURRENTLY_WORKING));
                workProfile.setWorkNature(workNatureRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkNature().getId()).get());
                workProfile.setWorkStatus(workStatusRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkStatus().getId()).get());
                workProfile.setExperienceInYears(hpWorkProfileUpdateRequestTO.getWorkDetails().getExperienceInYears());
                workProfileRepository.save(workProfile);

            }else {
                hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().forEach(currentWorkDetailsTO -> {
                    WorkProfile workProfile = new WorkProfile();
                    workProfileDetailsList.add(workProfileObjectMapping(hpWorkProfileUpdateRequestTO, workProfile, currentWorkDetailsTO, hpProfileId, userId));
                    workProfileRepository.saveAll(workProfileDetailsList);
                });
            }
        }
    }
}
