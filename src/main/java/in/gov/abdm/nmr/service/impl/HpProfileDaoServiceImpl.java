package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.nosql.entity.Council;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.ICouncilService;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static in.gov.abdm.nmr.util.NMRConstants.NO;
import static in.gov.abdm.nmr.util.NMRConstants.SUCCESS_RESPONSE;
import static in.gov.abdm.nmr.util.NMRUtil.coalesce;
import static in.gov.abdm.nmr.util.NMRUtil.validateWorkProfileDetailsAndProofs;

@Service
@Slf4j
public class HpProfileDaoServiceImpl implements IHpProfileDaoService {
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

    private static final List<String> SUPPORTED_FILE_TYPES = List.of(".pdf",".jpg",".jpeg",".png");


    public HpSmcDetailTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber) throws NoDataFoundException {
        HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();

        StateMedicalCouncil stateMedicalCouncil =
                stateMedicalCouncilRepository.findStateMedicalCouncilById(BigInteger.valueOf(councilId));

        String councilName = stateMedicalCouncil.getName();
        List<Council> councils = councilService.getCouncilByRegistrationNumberAndCouncilName(registrationNumber, councilName);

        if(councils.isEmpty()){
            throw new NoDataFoundException();
        }
        Council council = councils.get(0);

        hpSmcDetailTO.setHpName(council.getFullName());
        hpSmcDetailTO.setCouncilName(council.getRegistrationsDetails().get(0).getCouncilName());
        hpSmcDetailTO.setRegistrationNumber(council.getRegistrationsDetails().get(0).getRegistrationNo());
        hpSmcDetailTO.setEmailId(council.getEmail());
        hpSmcDetailTO.setAlreadyRegisteredInNmr(iRegistrationDetailRepository.existsByRegistrationNoAndStateMedicalCouncilId(registrationNumber,BigInteger.valueOf(councilId)));
        return hpSmcDetailTO;

    }

    @Override
    public HpProfileUpdateResponseTO updateHpPersonalDetails(BigInteger hpProfileId,
                                                             HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException {

        log.info("In HpProfileDaoServiceImpl : updateHpPersonalDetails method");

        HpProfile existingHpProfile = iHpProfileRepository.findById(NMRUtil.coalesce(hpProfileId, BigInteger.ZERO)).orElse(null);
        HpProfile copiedExistingHpProfile = existingHpProfile;
        HpProfile targetedHpProfile = null;
        BigInteger updatedHpProfileId = null;
        if (existingHpProfile == null || HpProfileStatus.APPROVED.getId().equals(existingHpProfile.getHpProfileStatus().getId())) {
            log.debug("Initiating HP Profile Insertion flow since there are no existing HP Profiles with this hp_profile_id or The existing HP Profile is now Approved");
            if (existingHpProfile != null) {
                log.debug("There was an existing HP Profile with the given hp_profile_id which has been Approved. ");
                HpProfile latestHpProfile = iHpProfileRepository.findLatestHpProfileByRegistrationId(existingHpProfile.getRegistrationId());
                if (HpProfileStatus.PENDING.getId().equals(latestHpProfile.getHpProfileStatus().getId())) {
                    throw new InvalidRequestException(NMRError.USE_LATEST_HP_ID.getCode(), NMRError.USE_LATEST_HP_ID.getMessage());
                }
            }

            targetedHpProfile = new HpProfile();
            if (existingHpProfile != null) {
                org.springframework.beans.BeanUtils.copyProperties(existingHpProfile, targetedHpProfile);
                targetedHpProfile.setId(null);
                targetedHpProfile.setIsNew(NO);
            }
            mapHpPersonalRequestToEntity(hpPersonalUpdateRequestTO, targetedHpProfile);
            HpProfile savedHpProfile = iHpProfileRepository.save(targetedHpProfile);
            updatedHpProfileId = savedHpProfile.getId();

        } else {
            log.debug("Initiating HP Profile Updation flow since there is an existing HP Profile with this hp_profile_id or The existing HP Profile is not yet Approved");
            mapHpPersonalRequestToEntity(hpPersonalUpdateRequestTO, existingHpProfile);
            updatedHpProfileId = existingHpProfile.getId();
        }
        if (hpPersonalUpdateRequestTO.getCommunicationAddress() != null && copiedExistingHpProfile != null) {
            Address address = NMRUtil.coalesce(iAddressRepository.getCommunicationAddressByHpProfileId(copiedExistingHpProfile.getId(), in.gov.abdm.nmr.enums.AddressType.COMMUNICATION.getId()), new Address());
            Address newAddress = new Address();
            org.springframework.beans.BeanUtils.copyProperties(address, newAddress);
            if (hpPersonalUpdateRequestTO.getCommunicationAddress().getId() != null) {
                newAddress.setId(address.getId());
            } else {
                newAddress.setId(null);
            }
            mapAddressRequestToEntity(updatedHpProfileId, hpPersonalUpdateRequestTO, newAddress);
            iAddressRepository.save(newAddress);
        }
        if (copiedExistingHpProfile != null && HpProfileStatus.APPROVED.getId().equals(copiedExistingHpProfile.getHpProfileStatus().getId())) {

            RegistrationDetails registrationDetails = iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(copiedExistingHpProfile.getId());
            RegistrationDetails newRegistrationDetails = new RegistrationDetails();
            org.springframework.beans.BeanUtils.copyProperties(registrationDetails, newRegistrationDetails);
            newRegistrationDetails.setId(null);
            newRegistrationDetails.setHpProfileId(targetedHpProfile);
            iRegistrationDetailRepository.save(newRegistrationDetails);

            Address kycAddress = NMRUtil.coalesce(iAddressRepository.getCommunicationAddressByHpProfileId(copiedExistingHpProfile.getId(), in.gov.abdm.nmr.enums.AddressType.KYC.getId()), new Address());
            if (kycAddress != null && kycAddress.getId() != null) {
                Address newKycAddress = new Address();
                org.springframework.beans.BeanUtils.copyProperties(kycAddress, newKycAddress);
                newKycAddress.setId(null);
                newKycAddress.setHpProfileId(updatedHpProfileId);
                iAddressRepository.save(newKycAddress);
            }
        }
        log.info("HpProfileDaoServiceImpl : updateHpPersonalDetails method : Execution Successful. ");
        return new HpProfileUpdateResponseTO(204, "Record Added/Updated Successfully!", updatedHpProfileId);
    }

    @SneakyThrows
    @Override
    public HpProfileUpdateResponseTO updateHpRegistrationDetails(BigInteger hpProfileId,
                                                                 HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, MultipartFile registrationCertificate, MultipartFile degreeCertificate) throws NmrException, InvalidRequestException {

        log.info("In HpProfileDaoServiceImpl : updateHpRegistrationDetails method");

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
            log.debug("Saving Qualification Details");
            saveQualificationDetails(hpProfile, registrationDetail, hpRegistrationUpdateRequestTO.getQualificationDetails(), degreeCertificate != null ? List.of(degreeCertificate) : Collections.emptyList());
        }

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
            validateWorkProfileDetailsAndProofs(hpWorkProfileUpdateRequestTO.getCurrentWorkDetails(), proofs);

            log.debug("Validation of Work Profile Details against the Proofs is successful. ");
            hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().stream().forEach(currentWorkDetailsTO -> {
                MultipartFile file = proofs.get(hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().indexOf(currentWorkDetailsTO));
                try {
                    currentWorkDetailsTO.setProof(file != null ? file.getBytes() : null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
                workProfile = workProfileRepository.getWorkProfileDetailsByUserId(userId);
            } else {
                throw new NotFoundException(NMRError.NO_MATCHING_USER_DETAILS_FOUND.getCode(), NMRError.NO_MATCHING_USER_DETAILS_FOUND.getMessage());
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
        List<BigInteger> languagesKnownIds = hpWorkProfileUpdateRequestTO.getLanguagesKnownIds();
        List<BigInteger> languagesKnownEarlierIds = new ArrayList<>();
        if (languagesKnownIds != null && !languagesKnownIds.isEmpty()) {
            List<LanguagesKnown> languagesKnownEarlier = languagesKnownRepository.findByUserId(userId);
            if (languagesKnownEarlier != null && !languagesKnownEarlier.isEmpty())
                languagesKnownEarlier.forEach(languagesKnown -> languagesKnownEarlierIds.add(languagesKnown.getLanguage().getId()));
        }
        List<LanguagesKnown> languagesKnownLater = new ArrayList<>();
        BigInteger tempUserId = userId;
        languagesKnownEarlierIds.forEach(languagesKnownEarlierId -> languagesKnownIds.remove(languagesKnownEarlierId));
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

    @Override
    public HpProfile findLatestEntryByUserid(BigInteger userId) {
        return iHpProfileRepository.findLatestEntryByUserid(userId);

    }

    @Override
    public HpProfilePictureResponseTO uploadHpProfilePhoto(MultipartFile file, BigInteger hpProfileId)
            throws IOException, InvalidRequestException {

        HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);
        if (hpProfile == null) {
            throw new InvalidRequestException(NMRError.INVALID_REQUEST.getCode(), NMRError.INVALID_REQUEST.getMessage());
        }

        if(file.getOriginalFilename() != null && !SUPPORTED_FILE_TYPES.contains(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase())){
            throw new InvalidRequestException(file.getOriginalFilename() + " is not a allowed file type !!");
        }
        String encodedPhoto = Base64.getEncoder().encodeToString(file.getBytes());

      /*  Map<String, String> form = new HashMap<>();

        form.put("grant_type", "client_credentials");

        byte[] encodedBytes = Base64Utils.encode((imageApiUsername + ":" + imageApiPassword).getBytes());

        String authHeader = "Basic " + new String(encodedBytes);

        ImageTokenTo imageTokenTo=imageFClient.getToken(authHeader,form);

        ProfileImageCompareTo imageCompareTo=new ProfileImageCompareTo(hpProfile.getProfilePhoto(),encodedPhoto);

        imageFClient.compareImages(imageCompareTo,"Bearer "+imageTokenTo.getAccessToken()));

        if condition here
        */

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
                mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification, !proofs.isEmpty() ? proofs.get(qualificationDetailRequestTOS.indexOf(indianQualification)) : null);
            } else {
                mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification, !proofs.isEmpty() ? proofs.get(qualificationDetailRequestTOS.indexOf(indianQualification)) : null);
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
        qualification.setIsVerified(indianQualification.getIsVerified());
        qualification.setQualificationYear(indianQualification.getQualificationYear());
        qualification.setQualificationMonth(indianQualification.getQualificationMonth());
        qualification.setIsNameChange(indianQualification.getIsNameChange());
        qualification.setRegistrationDetails(newRegistrationDetails);
        qualification.setHpProfile(hpProfile);
        qualification.setRequestId(
                coalesce(indianQualification.getRequestId(), hpProfile.getRequestId()));
        qualification.setBroadSpecialityId(indianQualification.getBroadSpecialityId());
        qualification.setSuperSpecialityName(indianQualification.getSuperSpecialityName());
        qualification.setUser(hpProfile.getUser());
        if (proof != null && !proof.isEmpty()) {
            qualification.setFileName(proof.getOriginalFilename());
            try {
                qualification.setCertificate(proof.getBytes());
            } catch (IOException e) {
                throw new InvalidRequestException();
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
                mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationalQualification, customQualification, !proofs.isEmpty() ? proofs.get(qualificationDetailRequestTOS.indexOf(internationalQualification)) : null);
            } else {
                mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationalQualification, customQualification, !proofs.isEmpty() ? proofs.get(qualificationDetailRequestTOS.indexOf(internationalQualification)) : null);
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
        //GK - 08-04-2023 - FE payload for additional qualification is coming as name, it needs to be corrected. Either courseName or name can rename in dto
        String courseName = newCustomQualification.getCourse().getCourseName();
        customQualification.setCourse(courseName != null ? courseName : newCustomQualification.getCourse().getName());
        customQualification.setIsVerified(newCustomQualification.getIsVerified());
        customQualification.setQualificationYear(newCustomQualification.getQualificationYear());
        customQualification.setQualificationMonth(newCustomQualification.getQualificationMonth());
        customQualification.setIsNameChange(newCustomQualification.getIsNameChange());
        customQualification.setRegistrationDetails(newRegistrationDetails);
        customQualification.setRequestId(
                coalesce(newCustomQualification.getRequestId(), hpProfile.getRequestId()));
        customQualification.setHpProfile(hpProfile);
        customQualification.setBroadSpecialityId(newCustomQualification.getBroadSpecialityId());
        customQualification.setSuperSpecialityName(newCustomQualification.getSuperSpecialityName());
        customQualification.setUser(hpProfile.getUser());
        if (proof != null && !proof.isEmpty()) {
            customQualification.setFileName(proof.getOriginalFilename());
            try {
                customQualification.setCertificate(proof.getBytes());
            } catch (IOException e) {
                throw new InvalidRequestException();
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

        District communicationDistrict = districtRepository
                .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getDistrict().getId())
                .orElse(null);
        addressData.setDistrict(communicationDistrict);

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
        hpProfile.setHpProfileStatus(in.gov.abdm.nmr.entity.HpProfileStatus.builder().id(HpProfileStatus.PENDING.getId()).build());
        hpProfile.setIsSameAddress(hpPersonalUpdateRequestTO.getCommunicationAddress().getIsSameAddress());
        Country countryNationality = countryRepository
                .findById(hpPersonalUpdateRequestTO.getPersonalDetails().getCountryNationality().getId())
                .orElse(null);
        hpProfile.setCountryNationality(countryNationality);
        hpProfile.setFullName(hpPersonalUpdateRequestTO.getPersonalDetails().getFullName());
        hpProfile.setEmailId(hpPersonalUpdateRequestTO.getCommunicationAddress().getEmail());
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
    private void mapWorkRequestToEntity(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<WorkProfile> addWorkProfiles, BigInteger hpProfileId, BigInteger userId) {
        if (!addWorkProfiles.isEmpty()) {
            List<String> facilityIdList = new ArrayList<>();
            hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().forEach(currentWorkDetailsTO -> facilityIdList.add(currentWorkDetailsTO.getFacilityId()));
            addWorkProfiles.forEach(workProfile -> facilityIdList.remove(workProfile.getFacilityId()));
            updateWorkProfileRecords(hpWorkProfileUpdateRequestTO, addWorkProfiles, hpProfileId, userId, facilityIdList);
        } else {
            saveWorkProfileRecords(hpWorkProfileUpdateRequestTO, hpProfileId, userId);
        }
    }

    private WorkProfile workProfileObjectMapping(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, WorkProfile addWorkProfile, CurrentWorkDetailsTO currentWorkDetailsTO, BigInteger hpProfileId, BigInteger userId) {
        addWorkProfile.setRequestId(hpWorkProfileUpdateRequestTO.getRequestId());
        addWorkProfile.setHpProfileId(hpProfileId);
        if (hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkNature() != null) {
            addWorkProfile.setWorkNature(workNatureRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkNature().getId()).get());
        }
        if (hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkStatus() != null) {
            addWorkProfile.setWorkStatus(workStatusRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkStatus().getId()).get());
        }
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
        addWorkProfile.setExperienceInYears(currentWorkDetailsTO.getExperienceInYears());
        addWorkProfile.setUserId(userId);
        return addWorkProfile;
    }

    private void updateWorkProfileRecords(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<WorkProfile> addWorkProfiles,
                                          BigInteger hpProfileId, BigInteger userId, List<String> facilityIdList) {
        List<WorkProfile> workProfileDetailsList = new ArrayList<>();
        addWorkProfiles.forEach(addWorkProfile -> {
            hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().forEach(currentWorkDetailsTO -> {
                if (addWorkProfile.getFacilityId().equals(currentWorkDetailsTO.getFacilityId())) {
                    workProfileDetailsList.add(workProfileObjectMapping(hpWorkProfileUpdateRequestTO, addWorkProfile, currentWorkDetailsTO, hpProfileId, userId));
                    workProfileRepository.saveAll(workProfileDetailsList);
                }
            });
        });
        if (!facilityIdList.isEmpty() && facilityIdList != null && addWorkProfiles != null) {
            facilityIdList.forEach(facilityId ->
                    hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().forEach(currentWorkDetailsTO -> {
                        if (facilityId.equals(currentWorkDetailsTO.getFacilityId())) {
                            WorkProfile workProfile = new WorkProfile();
                            workProfileDetailsList.add(workProfileObjectMapping(hpWorkProfileUpdateRequestTO, workProfile, currentWorkDetailsTO, hpProfileId, userId));
                            workProfileRepository.saveAll(workProfileDetailsList);
                        }
                    }));
        }
    }

    private void saveWorkProfileRecords(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, BigInteger hpProfileId, BigInteger userId) {
        List<WorkProfile> workProfileDetailsList = new ArrayList<>();
        hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().forEach(currentWorkDetailsTO -> {
            WorkProfile workProfile = new WorkProfile();
            workProfileDetailsList.add(workProfileObjectMapping(hpWorkProfileUpdateRequestTO, workProfile, currentWorkDetailsTO, hpProfileId, userId));
            workProfileRepository.saveAll(workProfileDetailsList);
        });
    }


}
