package in.gov.abdm.nmr.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.client.DscFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NoDataFoundException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.IHpProfileMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.NO_DATA_FOUND;
import static in.gov.abdm.nmr.util.NMRUtil.coalesce;

@Service
public class HpProfileDaoServiceImpl implements IHpProfileDaoService {
    @Autowired
    private IHpProfileMapper ihHpProfileMapper;
    @Autowired
    IHpProfileRepository iHpProfileRepository;
    @Autowired
    private IAddressRepository iAddressRepository;
    @Autowired
    private IRegistrationDetailRepository registrationDetailRepository;
    @Autowired
    private IQualificationDetailRepository qualificationDetailRepository;
    @Autowired
    private WorkProfileRepository workProfileRepository;
    @Autowired
    private SuperSpecialityRepository superSpecialityRepository;
    @Autowired
    IForeignQualificationDetailRepository iForeignQualificationDetailRepository;
    @Autowired
    IQualificationDetailRepository iQualificationDetailRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private IStateRepository stateRepository;
    @Autowired
    private IAddressTypeRepository iAddressTypeRepository;
    @Autowired
    private OrganizationTypeRepository organizationTypeRepository;
    @Autowired
    private IStateMedicalCouncilStatusRepository iStateMedicalCouncilStatusRepository;
    @Autowired
    private INationalityRepository iNationalityRepository;
    @Autowired
    private IScheduleRepository iScheduleRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private LanguagesKnownRepository languagesKnownRepository;

    @Autowired
    IRegistrationDetailRepository iRegistrationDetailRepository;
    @Autowired
    private IStateMedicalCouncilRepository iStateMedicalCouncilRepository;
    @Autowired
    private WorkNatureRepository workNatureRepository;
    @Autowired
    private WorkStatusRepository workStatusRepository;
    @Autowired
    private SubDistrictRepository subDistrictRepository;
    @Autowired
    private VillagesRepository villagesRepository;
    @Autowired
    private IForeignQualificationDetailRepository iCustomQualificationDetailRepository;
    @Autowired
    private HpNbeDetailsRepository hpNbeDetailsRepository;
    @Autowired
    private IRequestCounterService requestCounterService;
    @Autowired
    private DscFClient dscFClient;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ICollegeRepository collegeRepository;
    @Autowired
    private IUniversityRepository universityRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private BroadSpecialityRepository broadSpecialityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public HpSmcDetailTO fetchSmcRegistrationDetail(Integer councilId, String registrationNumber) {
        HpSmcDetailTO hpSmcDetailTO = new HpSmcDetailTO();

        Tuple hpProfile = iHpProfileRepository.fetchSmcRegistrationDetail(registrationNumber, councilId);
        if (hpProfile != null) {
            hpSmcDetailTO.setHpName(hpProfile.get("full_name", String.class));
            hpSmcDetailTO.setCouncilName(hpProfile.get("name", String.class));
            hpSmcDetailTO.setRegistrationNumber(hpProfile.get("registration_no", String.class));
            hpSmcDetailTO.setHpProfileId(hpProfile.get("hp_profile_id", BigInteger.class));
        } else {
            throw new NoDataFoundException(NO_DATA_FOUND);
        }
        return hpSmcDetailTO;
    }

    @Override
    public HpProfileUpdateResponseTO updateHpPersonalDetails(BigInteger hpProfileId,
                                                             HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO) throws InvalidRequestException, WorkFlowException {
        HpProfile existingHpProfile = iHpProfileRepository.findById(NMRUtil.coalesce(hpProfileId, BigInteger.ZERO)).orElse(null);
        HpProfile copiedExistingHpProfile = existingHpProfile;
        HpProfile targetedHpProfile = null;
        BigInteger updatedHpProfileId = null;
        if (existingHpProfile == null || HpProfileStatus.APPROVED.getId().equals(existingHpProfile.getHpProfileStatus().getId())) {
            existingHpProfile = new HpProfile();
            mapHpPersonalRequestToEntity(hpPersonalUpdateRequestTO, existingHpProfile);
            targetedHpProfile = iHpProfileRepository.save(existingHpProfile);
            updatedHpProfileId = targetedHpProfile.getId();

        } else {
            mapHpPersonalRequestToEntity(hpPersonalUpdateRequestTO, existingHpProfile);
            updatedHpProfileId = existingHpProfile.getId();
        }
        if (hpPersonalUpdateRequestTO.getCommunicationAddress() != null) {
            Address address = NMRUtil.coalesce(iAddressRepository.getCommunicationAddressByHpProfileId(existingHpProfile.getId(), in.gov.abdm.nmr.enums.AddressType.COMMUNICATION.getId()), new Address());
            mapAddressRequestToEntity(existingHpProfile.getId(), hpPersonalUpdateRequestTO, address);
            iAddressRepository.save(address);
        }
        List<LanguageTO> languages = hpPersonalUpdateRequestTO.getPersonalDetails().getLanguage();
        if (languagesKnownRepository.existsById(existingHpProfile.getId())) {
            languagesKnownRepository.deleteById(existingHpProfile.getId());
        }
        List<LanguagesKnown> languagesKnowns = new ArrayList<>();
        for (LanguageTO languageTO : languages) {
            languagesKnowns.add(LanguagesKnown.builder().hpProfile(existingHpProfile).languageId(languageTO.getId()).build());
        }
        languagesKnownRepository.saveAll(languagesKnowns);


        if (copiedExistingHpProfile != null) {

            if (HpProfileStatus.APPROVED.getId().equals(copiedExistingHpProfile.getHpProfileStatus().getId())) {

                RegistrationDetails registrationDetails = iRegistrationDetailRepository.getRegistrationDetailsByHpProfileId(copiedExistingHpProfile.getId());
                RegistrationDetails newRegistrationDetails = new RegistrationDetails();
                org.springframework.beans.BeanUtils.copyProperties(registrationDetails, newRegistrationDetails);
                newRegistrationDetails.setId(null);
                newRegistrationDetails.setHpProfileId(targetedHpProfile);
                iRegistrationDetailRepository.save(newRegistrationDetails);

                WorkProfile workProfile = workProfileRepository.getWorkProfileByHpProfileId(copiedExistingHpProfile.getId());
                WorkProfile newWorkProfile = new WorkProfile();
                org.springframework.beans.BeanUtils.copyProperties(workProfile, newWorkProfile);
                newWorkProfile.setId(null);
                newWorkProfile.setHpProfileId(targetedHpProfile.getId());
                workProfileRepository.save(newWorkProfile);

                List<LanguagesKnown> languagesKnownList = new ArrayList<>();
                List<LanguagesKnown> languagesKnown = languagesKnownRepository.getLanguagesKnownByHpProfileId(copiedExistingHpProfile.getId());
                for (LanguagesKnown languageKnown : languagesKnown) {
                    LanguagesKnown newLanguagesKnown = new LanguagesKnown();
                    org.springframework.beans.BeanUtils.copyProperties(languageKnown, newLanguagesKnown);
                    newLanguagesKnown.setId(null);
                    newLanguagesKnown.setHpProfile(targetedHpProfile);
                    languagesKnownList.add(newLanguagesKnown);
                }
                languagesKnownRepository.saveAll(languagesKnownList);

                List<QualificationDetails> qualificationDetails = new ArrayList<>();
                List<QualificationDetails> qualificationDetailsList = iQualificationDetailRepository.getQualificationDetailsByHpProfileId(copiedExistingHpProfile.getId());
                for (QualificationDetails qualificationDetail : qualificationDetailsList) {
                    QualificationDetails newQualificationDetails = new QualificationDetails();
                    org.springframework.beans.BeanUtils.copyProperties(qualificationDetail, newQualificationDetails);
                    newQualificationDetails.setId(null);
                    newQualificationDetails.setHpProfile(targetedHpProfile);
                    qualificationDetails.add(newQualificationDetails);
                }
                iQualificationDetailRepository.saveAll(qualificationDetails);

                List<ForeignQualificationDetails> customQualificationDetailsList = new ArrayList<>();
                List<ForeignQualificationDetails> customQualificationDetails = iForeignQualificationDetailRepository.getQualificationDetailsByHpProfileId(copiedExistingHpProfile.getId());
                for (ForeignQualificationDetails customQualificationDetail : customQualificationDetails) {
                    ForeignQualificationDetails newCustomQualificationDetails = new ForeignQualificationDetails();
                    org.springframework.beans.BeanUtils.copyProperties(customQualificationDetail, newCustomQualificationDetails);
                    newCustomQualificationDetails.setId(null);
                    newCustomQualificationDetails.setHpProfile(targetedHpProfile);
                    customQualificationDetailsList.add(newCustomQualificationDetails);
                }
                iForeignQualificationDetailRepository.saveAll(customQualificationDetailsList);

                List<SuperSpeciality> superSpecialities = new ArrayList<>();
                List<SuperSpeciality> superSpecialityList = superSpecialityRepository.getSuperSpecialityFromHpProfileId(copiedExistingHpProfile.getId());
                for (SuperSpeciality superSpeciality : superSpecialityList) {
                    SuperSpeciality newSuperSpeciality = new SuperSpeciality();
                    org.springframework.beans.BeanUtils.copyProperties(superSpeciality, newSuperSpeciality);
                    newSuperSpeciality.setId(null);
                    newSuperSpeciality.setHpProfileId(targetedHpProfile.getId());
                    superSpecialities.add(newSuperSpeciality);
                }
                superSpecialityRepository.saveAll(superSpecialities);
            }
        }

        return new HpProfileUpdateResponseTO(204, "Record Added/Updated Successfully!", updatedHpProfileId);
    }

    @Override
    public HpProfileUpdateResponseTO updateHpRegistrationDetails(BigInteger hpProfileId,
                                                                 String hpRegistrationUpdateRequestString, MultipartFile certificate, MultipartFile proof) {

        HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO = getHpRegistrationUpdateRequestTO(hpRegistrationUpdateRequestString);
        if (hpRegistrationUpdateRequestTO.getRegistrationDetail() != null) {
            hpRegistrationUpdateRequestTO.getRegistrationDetail().setCertificate(certificate);
            hpRegistrationUpdateRequestTO.getRegistrationDetail().setNameChangeProof(proof);
        }


        RegistrationDetails registrationDetail = registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
        HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);

        if (registrationDetail == null) {
            registrationDetail = new RegistrationDetails();
            mapRegistrationRequestToEntity(hpRegistrationUpdateRequestTO, registrationDetail, hpProfile);
            registrationDetailRepository.save(registrationDetail);
        } else {
            mapRegistrationRequestToEntity(hpRegistrationUpdateRequestTO, registrationDetail, hpProfile);
        }
        saveQualificationDetails(hpProfile, registrationDetail, hpRegistrationUpdateRequestTO.getQualificationDetail());
        HpNbeDetails hpNbeDetails = hpNbeDetailsRepository.findByHpProfileId(hpProfileId);
        if (hpNbeDetails == null) {
            hpNbeDetails = new HpNbeDetails();
            mapNbeRequestDetailsToEntity(hpRegistrationUpdateRequestTO, hpNbeDetails, hpProfile);
            hpNbeDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        } else {
            mapNbeRequestDetailsToEntity(hpRegistrationUpdateRequestTO, hpNbeDetails, hpProfile);
        }
        hpNbeDetailsRepository.save(hpNbeDetails);
        return new HpProfileUpdateResponseTO(204,
                "Registration Added/Updated Successfully!!", hpProfileId);
    }

    @Override
    public HpProfileUpdateResponseTO updateWorkProfileDetails(BigInteger hpProfileId,
                                                              String hpWorkProfileUpdateRequestString, MultipartFile proof) {

        HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO = getUpdateWorkProfileDetailsTo(hpWorkProfileUpdateRequestString);

        hpWorkProfileUpdateRequestTO.getCurrentWorkDetails().stream().forEach(currentWorkDetailsTO -> {
            currentWorkDetailsTO.setProof(proof);
        });

        List<WorkProfile> workProfile = workProfileRepository.getWorkProfileDetailsByHPId(hpProfileId);
        if (workProfile == null) {
            workProfile = new ArrayList<>();
            mapWorkRequestToEntity(hpWorkProfileUpdateRequestTO, workProfile, hpProfileId);
            workProfileRepository.saveAll(workProfile);
        } else {
            mapWorkRequestToEntity(hpWorkProfileUpdateRequestTO, workProfile, hpProfileId);
        }
        List<SuperSpecialityTO> newSuperSpecialities = hpWorkProfileUpdateRequestTO.getSpecialityDetails().getSuperSpeciality();
        List<SuperSpeciality> superSpecialities = new ArrayList<>();
        for (SuperSpecialityTO superSpecialityTo : newSuperSpecialities) {
            SuperSpeciality superSpecialityEntity = new SuperSpeciality();
            if (superSpecialityTo.getId() != null) {
                superSpecialityEntity = superSpecialityRepository.findById(superSpecialityTo.getId()).get();
                mapSuperSpecialityToEntity(hpProfileId, superSpecialityTo, superSpecialityEntity);
            } else {
                mapSuperSpecialityToEntity(hpProfileId, superSpecialityTo, superSpecialityEntity);
                superSpecialities.add(superSpecialityEntity);
            }
        }
        superSpecialityRepository.saveAll(superSpecialities);
        return new HpProfileUpdateResponseTO(204,
                "Registration Added/Updated Successfully!!", hpProfileId);
    }

    @Override
    public void saveQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, List<QualificationDetailRequestTO> qualificationDetailRequestTOS) {
        if (qualificationDetailRequestTOS != null) {
            saveIndianQualificationDetails(hpProfile, newRegistrationDetails, qualificationDetailRequestTOS.stream().filter(qualificationDetailRequestTO -> NMRConstants.INDIA.equals(qualificationDetailRequestTO.getQualificationFrom())).toList());
            saveInternationalQualificationDetails(hpProfile, newRegistrationDetails, qualificationDetailRequestTOS.stream().filter(qualificationDetailRequestTO -> NMRConstants.INTERNATIONAL.equals(qualificationDetailRequestTO.getQualificationFrom())).toList());
        }
    }

    @Override
    public HpProfile findByUserDetail(BigInteger userDetailId) {
        return iHpProfileRepository.findByUserDetail(userDetailId);
    }

    @Override
    public HpProfilePictureResponseTO uploadHpProfilePhoto(MultipartFile file, BigInteger hpProfileId)
            throws IOException {

        HpProfile hpProfile = iHpProfileRepository.findById(hpProfileId).orElse(null);
        if (hpProfile == null) {
            throw new InvalidRequestException("Invalid Request!!");
        }
        String originalHpProfilePhoto = file.getOriginalFilename();

        if (originalHpProfilePhoto.toLowerCase().contains(".pdf")
                || originalHpProfilePhoto.toLowerCase().contains(".jpg")
                || originalHpProfilePhoto.toLowerCase().contains(".jpeg")
                || originalHpProfilePhoto.toLowerCase().contains(".png")) {
        } else {
            throw new InvalidRequestException(file.getOriginalFilename() + " is not a allowed file type !!");
        }

        byte[] fileContent = file.getBytes();

        String encodedHpProfilePhoto = Base64.getEncoder().encodeToString(fileContent);
        hpProfile.setProfilePhoto(fileContent);
        hpProfile.setPicName(file.getOriginalFilename());
        HpProfile insertedData = iHpProfileRepository.save(hpProfile);
        HpProfilePictureResponseTO hpProfilePictureResponseTO = new HpProfilePictureResponseTO();

        if (insertedData == null) {
            hpProfilePictureResponseTO.setMessage("Success");
            hpProfilePictureResponseTO.setStatus(200);
            return hpProfilePictureResponseTO;
        }

        hpProfilePictureResponseTO.setProfilePicture(insertedData.getProfilePhoto());
        hpProfilePictureResponseTO.setPicName(insertedData.getPicName());
        hpProfilePictureResponseTO.setMessage("Success");
        hpProfilePictureResponseTO.setStatus(200);
        return hpProfilePictureResponseTO;
    }


    @Override
    public HpProfile findById(BigInteger id) {
        return iHpProfileRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseMessageTo setHpProfilePhotoAndAddressThroughAadhaar(BigInteger id, AadhaarUserKycTo userKycTo) {

        String addressLine = checkIsNullAndAddSeparator(userKycTo.getHouse())
                + checkIsNullAndAddSeparator(userKycTo.getStreet())
                + checkIsNullAndAddSeparator(userKycTo.getLandmark())
                + checkIsNullAndAddSeparator(userKycTo.getVillageTownCity())
                + checkIsNullAndAddSeparator(userKycTo.getLocality())
                + checkIsNullAndAddSeparator(userKycTo.getPincode())
                + checkIsNullAndAddSeparator(userKycTo.getPostOffice());
        try {
            HpProfile hpProfile = iHpProfileRepository.findHpProfileById(id);
            hpProfile.setProfilePhoto(userKycTo.getPhoto().getBytes(StandardCharsets.UTF_8));
            iHpProfileRepository.save(hpProfile);

            Address address = iAddressRepository.getCommunicationAddressByHpProfileId(id,
                    NMRConstants.DEFAULT_ADDRESS_TYPE_AADHAR);
            address.setAddressLine1(addressLine);

            Villages village = villagesRepository.findByName(userKycTo.getVillageTownCity());
            if (village != null) {
                address.setVillage(village);
            }

            SubDistrict subDistrict = subDistrictRepository.findByName(userKycTo.getSubDist());
            if (subDistrict != null) {
                address.setSubDistrict(subDistrict);
            }
            District district = districtRepository.findByName(userKycTo.getDistrict());
            if (district != null) {
                address.setDistrict(district);
            }
            State state = stateRepository.findByName(userKycTo.getState());
            if (state != null) {
                address.setState(state);
            }

            Country country = countryRepository.findByName(NMRConstants.DEFAULT_COUNTRY_AADHAR);
            if (country != null) {
                address.setCountry(country);
            }

            address.setPincode(userKycTo.getPincode());
            address.setEmail(userKycTo.getEmail());
            address.setMobile(userKycTo.getPhone());
            iAddressRepository.save(address);
        } catch (Exception e) {
            return new ResponseMessageTo(NMRConstants.FAILURE_RESPONSE);
        }
        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
    }

    private void saveIndianQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
                                                List<QualificationDetailRequestTO> qualificationDetailRequestTOS) {
        if (qualificationDetailRequestTOS.size() > 0) {
            List<QualificationDetails> qualificationDetails = new ArrayList<>();
            for (QualificationDetailRequestTO indianQualification : qualificationDetailRequestTOS) {
                QualificationDetails qualification = new QualificationDetails();
                if (indianQualification.getId() != null) {
                    qualification = qualificationDetailRepository.findById(indianQualification.getId()).get();
                    mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification);
                } else {
                    mapIndianQualificationRequestToEntity(hpProfile, newRegistrationDetails, indianQualification, qualification);
                    qualificationDetails.add(qualification);
                }
            }
            qualificationDetailRepository.saveAll(qualificationDetails);
        }
    }

    private void mapIndianQualificationRequestToEntity(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, QualificationDetailRequestTO indianQualification, QualificationDetails qualification) {
        qualification.setCountry(countryRepository.findById(indianQualification.getCountry().getId()).get());
        qualification.setState(stateRepository.findById(indianQualification.getState().getId()).get());
        qualification.setCollege(collegeRepository.findById(indianQualification.getCollege().getId()).get());
        qualification.setUniversity(universityRepository.findById(indianQualification.getUniversity().getId()).get());
        qualification.setCourse(courseRepository.findById(indianQualification.getCourse().getId()).get());
        qualification.setIsVerified(indianQualification.getIsVerified());
        qualification.setQualificationYear(indianQualification.getQualificationYear());
        qualification.setQualificationMonth(indianQualification.getQualificationMonth());
        qualification.setIsNameChange(indianQualification.getIsNameChange());
        qualification.setRegistrationDetails(newRegistrationDetails);
        qualification.setHpProfile(hpProfile);
        qualification.setRequestId(
                coalesce(indianQualification.getRequestId(), hpProfile.getRequestId()));
    }


    private void saveInternationalQualificationDetails(HpProfile hpProfile, RegistrationDetails newRegistrationDetails,
                                                       List<QualificationDetailRequestTO> qualificationDetailRequestTOS) {
        if (qualificationDetailRequestTOS.size() > 0) {
            List<ForeignQualificationDetails> internationQualifications = new ArrayList<ForeignQualificationDetails>();
            for (QualificationDetailRequestTO internationQualification : qualificationDetailRequestTOS) {
                ForeignQualificationDetails customQualification = new ForeignQualificationDetails();
                if (internationQualification.getId() != null) {
                    customQualification = iCustomQualificationDetailRepository.findById(internationQualification.getId()).get();
                    mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationQualification, customQualification);
                } else {
                    mapQualificationRequestToEntity(hpProfile, newRegistrationDetails, internationQualification, customQualification);
                    internationQualifications.add(customQualification);
                }
            }
            iCustomQualificationDetailRepository.saveAll(internationQualifications);
        }
    }

    private void mapQualificationRequestToEntity(HpProfile hpProfile, RegistrationDetails newRegistrationDetails, QualificationDetailRequestTO newCustomQualification, ForeignQualificationDetails customQualification) {
        customQualification.setCountry(newCustomQualification.getCountry().getName());
        customQualification.setState(newCustomQualification.getState().getName());
        customQualification.setCollege(newCustomQualification.getCollege().getName());
        customQualification.setUniversity(newCustomQualification.getUniversity().getName());
        customQualification.setCourse(newCustomQualification.getCourse().getCourseName());
        customQualification.setIsVerified(newCustomQualification.getIsVerified());
        customQualification.setQualificationYear(newCustomQualification.getQualificationYear());
        customQualification.setQualificationMonth(newCustomQualification.getQualificationMonth());
        customQualification.setIsNameChange(newCustomQualification.getIsNameChange());
        customQualification.setRegistrationDetails(newRegistrationDetails);
        customQualification.setRequestId(
                coalesce(newCustomQualification.getRequestId(), hpProfile.getRequestId()));
        customQualification.setHpProfile(hpProfile);
    }

    private void mapAddressRequestToEntity(BigInteger hpProfileId, HpPersonalUpdateRequestTO hpPersonalUpdateRequestTO, Address addressData) {
        addressData.setAddressLine1(hpPersonalUpdateRequestTO.getCommunicationAddress().getAddressLine1());
        addressData.setPincode(hpPersonalUpdateRequestTO.getCommunicationAddress().getPincode());
        addressData.setEmail(hpPersonalUpdateRequestTO.getCommunicationAddress().getEmail());
        addressData.setMobile(hpPersonalUpdateRequestTO.getCommunicationAddress().getMobile());

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

        SubDistrict communicationSubDistrict = subDistrictRepository
                .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getSubDistrict().getId())
                .orElse(null);
        addressData.setSubDistrict(communicationSubDistrict);

        Villages communicationCity = villagesRepository
                .findById(hpPersonalUpdateRequestTO.getCommunicationAddress().getVillage().getId())
                .orElse(null);
        addressData.setVillage(communicationCity);

        AddressType addressType = new AddressType();
        addressType.setId(hpPersonalUpdateRequestTO.getCommunicationAddress().getAddressType().getId());
        addressData.setAddressTypeId(addressType);

        addressData.setHpProfileId(hpProfileId);
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
        hpProfile.setRequestId(hpPersonalUpdateRequestTO.getRequestId());
        hpProfile.setRegistrationId(hpPersonalUpdateRequestTO.getImrDetails().getRegistrationNumber().toString());
        hpProfile.setHpProfileStatus(in.gov.abdm.nmr.entity.HpProfileStatus.builder().id(HpProfileStatus.PENDING.getId()).build());

        Schedule schedule = iScheduleRepository
                .findById(hpPersonalUpdateRequestTO.getPersonalDetails().getSchedule().getId()).orElse(null);
        hpProfile.setSchedule(schedule);

        Country countryNationality = countryRepository
                .findById(hpPersonalUpdateRequestTO.getPersonalDetails().getCountryNationality().getId())
                .orElse(null);
        hpProfile.setCountryNationality(countryNationality);

        hpProfile.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        hpProfile.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        hpProfile.setNmrId(hpPersonalUpdateRequestTO.getImrDetails().getNmrId());
        hpProfile.setYearOfInfo(hpPersonalUpdateRequestTO.getImrDetails().getYearOfInfo());

        hpProfile.setFullName(hpPersonalUpdateRequestTO.getCommunicationAddress().getFullName());
    }

    private void mapNbeRequestDetailsToEntity(HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, HpNbeDetails hpNbeDetails, HpProfile hpProfile) {
        if (hpRegistrationUpdateRequestTO.getHpNbeDetails() != null) {
            hpNbeDetails.setMarksObtained(hpRegistrationUpdateRequestTO.getHpNbeDetails().getMarksObtained());
            hpNbeDetails.setMonthOfPassing(hpRegistrationUpdateRequestTO.getHpNbeDetails().getMonthOfPassing());
            hpNbeDetails.setRollNo(hpRegistrationUpdateRequestTO.getHpNbeDetails().getRollNo());
            hpNbeDetails.setUserResult(hpRegistrationUpdateRequestTO.getHpNbeDetails().getUserResult());
            hpNbeDetails.setYearOfPassing(hpRegistrationUpdateRequestTO.getHpNbeDetails().getYearOfPassing());
            hpNbeDetails.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            hpNbeDetails.setHpProfileId(hpProfile.getId());
        }
    }

    @SneakyThrows
    private void mapRegistrationRequestToEntity(HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO, RegistrationDetails registrationDetail, HpProfile hpProfile) {
        if (hpRegistrationUpdateRequestTO.getRegistrationDetail() != null) {
            registrationDetail.setRegistrationDate(hpRegistrationUpdateRequestTO.getRegistrationDetail().getRegistrationDate());
            registrationDetail.setRegistrationNo(hpRegistrationUpdateRequestTO.getRegistrationDetail().getRegistrationNumber());
            registrationDetail.setStateMedicalCouncil(iStateMedicalCouncilRepository
                    .findById(hpRegistrationUpdateRequestTO.getRegistrationDetail().getStateMedicalCouncil().getId()).get());
            registrationDetail.setIsRenewable(hpRegistrationUpdateRequestTO.getRegistrationDetail().getIsRenewable());
            registrationDetail.setRenewableRegistrationDate(
                    hpRegistrationUpdateRequestTO.getRegistrationDetail().getRenewableRegistrationDate());
            registrationDetail.setIsNameChange(hpRegistrationUpdateRequestTO.getRegistrationDetail().getIsNameChange());
            registrationDetail.setCertificate(hpRegistrationUpdateRequestTO.getRegistrationDetail().getCertificate().getBytes());
            registrationDetail.setNameChangeProofAttachment(hpRegistrationUpdateRequestTO.getRegistrationDetail().getNameChangeProof().getBytes());
        }
        registrationDetail.setHpProfileId(hpProfile);
        registrationDetail.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        registrationDetail.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    }

    private void mapSuperSpecialityToEntity(BigInteger hpProfileId, SuperSpecialityTO speciality, SuperSpeciality superSpeciality) {
        superSpeciality.setName(speciality.getName());
        superSpeciality.setHpProfileId(hpProfileId);
    }

    @SneakyThrows
    private void mapWorkRequestToEntity(HpWorkProfileUpdateRequestTO hpWorkProfileUpdateRequestTO, List<WorkProfile> addWorkProfiles, BigInteger hpProfileId) {
        addWorkProfiles.stream().forEach(addWorkProfile -> {
            addWorkProfile.setBroadSpeciality(broadSpecialityRepository.findById(hpWorkProfileUpdateRequestTO.getSpecialityDetails().getBroadSpeciality().getId()).get());

            addWorkProfile.setWorkNature(workNatureRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkNature().getId()).get());
            addWorkProfile.setWorkStatus(workStatusRepository.findById(hpWorkProfileUpdateRequestTO.getWorkDetails().getWorkStatus().getId()).get());
            addWorkProfile.setIsUserCurrentlyWorking(
                    hpWorkProfileUpdateRequestTO.getWorkDetails().getIsUserCurrentlyWorking());
            List<CurrentWorkDetailsTO> currentWorkDetailsTOList = hpWorkProfileUpdateRequestTO.getCurrentWorkDetails();
            currentWorkDetailsTOList.stream().filter(currentWorkDetailsTOFilter -> currentWorkDetailsTOFilter.getFacilityId() == addWorkProfile.getFacilityId()).forEach(currentWorkDetailsTO -> {
                addWorkProfile.setFacilityId(currentWorkDetailsTO.getFacilityId());
                addWorkProfile.setFacilityTypeId(currentWorkDetailsTO.getFacilityTypeId());
                addWorkProfile.setUrl(currentWorkDetailsTO.getUrl());
                addWorkProfile
                        .setWorkOrganization(currentWorkDetailsTO.getWorkOrganization());
                addWorkProfile.setOrganizationType(currentWorkDetailsTO.getOrganizationType());
                if (currentWorkDetailsTO.getAddress() != null) {
                    addWorkProfile.setAddress(currentWorkDetailsTO.getAddress().getAddressLine1());
                    addWorkProfile.setState(stateRepository.findById(currentWorkDetailsTO.getAddress().getState().getId()).get());
                    addWorkProfile.setDistrict(districtRepository.findById(currentWorkDetailsTO.getAddress().getDistrict().getId()).get());
                    addWorkProfile.setPincode(currentWorkDetailsTO.getAddress().getPincode());
                }
                try {
                    addWorkProfile.setProofOfWorkAttachment(currentWorkDetailsTO.getProof().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            addWorkProfile.setRequestId(hpWorkProfileUpdateRequestTO.getRequestId());
            addWorkProfile.setHpProfileId(hpProfileId);
        });
    }

    private String checkIsNullAndAddSeparator(String string) {
        if (string == null) {
            return "";
        } else {
            return string + ", ";
        }
    }

    @SneakyThrows
    private String multipartFileToBase64(MultipartFile file) {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    @SneakyThrows
    HpRegistrationUpdateRequestTO getHpRegistrationUpdateRequestTO(String hpRegistrationUpdateRequestString) {

        HpRegistrationUpdateRequestTO hpRegistrationUpdateRequestTO = objectMapper.readValue(hpRegistrationUpdateRequestString, HpRegistrationUpdateRequestTO.class);

        return hpRegistrationUpdateRequestTO;
    }

    @SneakyThrows
    HpWorkProfileUpdateRequestTO getUpdateWorkProfileDetailsTo(String getUpdateWorkProfileDetailsString) {

        HpWorkProfileUpdateRequestTO hpRegistrationUpdateRequestTO = objectMapper.readValue(getUpdateWorkProfileDetailsString, HpWorkProfileUpdateRequestTO.class);

        return hpRegistrationUpdateRequestTO;
    }

}
