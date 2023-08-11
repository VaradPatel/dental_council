package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.repository.IAddressMasterRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class NMRToHPRMapper {
    @Autowired
    IAddressMasterRepository addressMasterRepository;
    private static final String HEALTH_PROFESSIONAL_TYPE = "doctor";
    private static final String DOCTOR_SALUTATION = "1";
    private static final String ENGLISH_LANGUAGE = "1";
    private static final BigInteger CATEGORY_HEALTH_PROFESSIONAL = BigInteger.ONE;
    private static final BigInteger CATEGORY_ID_HEALTH_PROFESSIONAL = BigInteger.ONE;

    public PractitionerRequestTO convertNmrDataToHprRequestTo(HPRIdTokenResponseTO responseTO, HpProfileMaster masterHpProfileDetails,
                                                              RegistrationDetailsMaster registrationMaster, AddressMaster addressMaster,
                                                              List<QualificationDetailsMaster> qualificationDetailsMasterList,
                                                              List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList) {
        PractitionerRequestTO practitionerRequestTO = new PractitionerRequestTO();
        practitionerRequestTO.setHprToken(responseTO.getToken());
        practitionerRequestTO.setPractitioner(setPractitionerDetails(masterHpProfileDetails, registrationMaster, addressMaster, qualificationDetailsMasterList, foreignQualificationDetailsMasterList));
        return practitionerRequestTO;
    }

    private PractitionerTO setPractitionerDetails(HpProfileMaster masterHpProfileDetails, RegistrationDetailsMaster registrationMaster, AddressMaster addressMaster, List<QualificationDetailsMaster> qualificationDetailsMasterList, List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList) {
        PractitionerTO practitionerTO = new PractitionerTO();
        practitionerTO.setProfilePhoto("");

        practitionerTO.setHealthProfessionalType(HEALTH_PROFESSIONAL_TYPE);
        practitionerTO.setOfficialMobile(masterHpProfileDetails.getMobileNumber());
        practitionerTO.setOfficialEmail(masterHpProfileDetails.getEmailId());
        AddressMaster kycAddress = addressMasterRepository.getCommunicationAddressByHpProfileId(masterHpProfileDetails.getId(), AddressType.KYC.getId());
        practitionerTO.setAddressAsPerKYC(kycAddress != null ? kycAddressBuilder(kycAddress) : "");


        practitionerTO.setPersonalInformation(populatePersonalInformationDetails(masterHpProfileDetails));
        practitionerTO.setRegistrationAcademic(populatePractitionerRegistrationDetails(registrationMaster, qualificationDetailsMasterList, foreignQualificationDetailsMasterList));
        practitionerTO.setCommunicationAddress(populatePractitionerAddress(masterHpProfileDetails, addressMaster));
        practitionerTO.setCurrentWorkDetails(populatePractitionerWorkDetails());
        return practitionerTO;
    }

    private String kycAddressBuilder(AddressMaster kycAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append(kycAddress.getAddressLine1());
        sb.append("  ");
        sb.append(kycAddress.getVillage() != null ? kycAddress.getVillage().getName() : "  ");
        sb.append("  ");
        sb.append(kycAddress.getSubDistrict() != null ? kycAddress.getSubDistrict().getName() : "  ");
        sb.append("  ");
        sb.append(kycAddress.getDistrict() != null ? kycAddress.getDistrict().getName() : "  ");
        sb.append("  ");
        sb.append(kycAddress.getState().getName());
        sb.append("  ");
        sb.append(kycAddress.getCountry().getName());
        return sb.toString();
    }

    private PractitionerPersonalInformationTO populatePersonalInformationDetails(HpProfileMaster masterHpProfileDetails) {
        PractitionerPersonalInformationTO personalInformationTO = new PractitionerPersonalInformationTO();
        personalInformationTO.setSalutation(DOCTOR_SALUTATION);
        personalInformationTO.setFirstName(masterHpProfileDetails.getFirstName());
        personalInformationTO.setMiddleName(masterHpProfileDetails.getMiddleName());
        personalInformationTO.setLastName(masterHpProfileDetails.getLastName());
        personalInformationTO.setFatherName(masterHpProfileDetails.getFatherName());
        personalInformationTO.setMotherName(masterHpProfileDetails.getMotherName());
        personalInformationTO.setSpouseName(masterHpProfileDetails.getSpouseName());
        personalInformationTO.setNationality(masterHpProfileDetails.getCountryNationality().getId().toString());
        personalInformationTO.setLanguagesSpoken(ENGLISH_LANGUAGE);
        return personalInformationTO;
    }

    private PractitionerAddressTO populatePractitionerAddress(HpProfileMaster masterHpProfileDetails, AddressMaster addressMaster) {
        PractitionerAddressTO communicationAddressTO = new PractitionerAddressTO();
        communicationAddressTO.setName(masterHpProfileDetails.getFullName());
        if (addressMaster != null) {
            communicationAddressTO.setAddress(addressMaster.getAddressLine1());
            communicationAddressTO.setCountry(addressMaster.getCountry() != null ? addressMaster.getCountry().getId().toString() : "");
            communicationAddressTO.setState(addressMaster.getState() != null ? addressMaster.getState().getIsoCode() : "");
            communicationAddressTO.setDistrict(addressMaster.getDistrict() != null ? addressMaster.getDistrict().getId().toString() : "");
            communicationAddressTO.setPostalCode(addressMaster.getPincode());
            communicationAddressTO.setSubDistrict(addressMaster.getSubDistrict() != null ? addressMaster.getSubDistrict().getId().toString() : "");
            communicationAddressTO.setCity(addressMaster.getVillage() != null ? addressMaster.getVillage().getId().toString() : "");
        }
        communicationAddressTO.setIsCommunicationAddressAsPerKYC("true".equals(masterHpProfileDetails.getIsSameAddress()) ? 1 : 0);
        return communicationAddressTO;
    }

    @SneakyThrows
    private PractitionerRegistrationDetailsTO populatePractitionerRegistrationDetails(RegistrationDetailsMaster registrationMaster, List<QualificationDetailsMaster> qualificationDetailsMasterList, List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList) {
        PractitionerRegistrationDetailsTO registrationDetailsTO = new PractitionerRegistrationDetailsTO();
        registrationDetailsTO.setCategory(CATEGORY_HEALTH_PROFESSIONAL);
        List<PractitionerRegistrationTO> practitionerRegistrationTOList = new ArrayList<>();
        PractitionerRegistrationTO practitionerRegistrationTO = new PractitionerRegistrationTO();
        practitionerRegistrationTO.setRegisteredWithCouncil(registrationMaster.getStateMedicalCouncil() != null ? registrationMaster.getStateMedicalCouncil().getId().toString() : null);
        practitionerRegistrationTO.setRegistrationNumber(registrationMaster.getRegistrationNo());
        CertificateTO registrationCertificateTO = new CertificateTO();
        if (registrationMaster.getCertificate() != null && StringUtils.isNotBlank(registrationMaster.getFileName())) {
            registrationCertificateTO.setData(Base64.getEncoder().encodeToString(registrationMaster.getCertificate()));
            registrationCertificateTO.setFileType(Files.probeContentType(Paths.get(registrationMaster.getFileName())));
            practitionerRegistrationTO.setRegistrationCertificate(registrationCertificateTO);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        practitionerRegistrationTO.setRegistrationDate(dateFormat.format(registrationMaster.getRegistrationDate()));
        practitionerRegistrationTO.setIsNameDifferentInCertificate(BigInteger.ZERO);
        practitionerRegistrationTO.setCategoryId(CATEGORY_ID_HEALTH_PROFESSIONAL);
        List<PractitionerQualififcationTO> practitionerQualififcationTOList = new ArrayList<>();
        PractitionerQualififcationTO practitionerQualififcationTO = new PractitionerQualififcationTO();
        addQualificationDetails(qualificationDetailsMasterList, foreignQualificationDetailsMasterList, practitionerQualififcationTO);
        practitionerQualififcationTOList.add(practitionerQualififcationTO);
        practitionerRegistrationTO.setQualifications(practitionerQualififcationTOList);
        practitionerRegistrationTOList.add(practitionerRegistrationTO);
        registrationDetailsTO.setRegistrationData(practitionerRegistrationTOList);
        return registrationDetailsTO;
    }

    private static void addQualificationDetails(List<QualificationDetailsMaster> qualificationDetailsMasterList, List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList, PractitionerQualififcationTO practitionerQualififcationTO) {
        if (qualificationDetailsMasterList != null && !qualificationDetailsMasterList.isEmpty()) {
            qualificationDetailsMasterList.forEach(qualificationDetailsMaster -> {
                practitionerQualififcationTO.setNameOfDegreeOrDiplomaObtained(qualificationDetailsMaster.getCourse() != null ? qualificationDetailsMaster.getCourse().getId() : null);
                practitionerQualififcationTO.setCountry(qualificationDetailsMaster.getCountry() != null ? qualificationDetailsMaster.getCountry().getId() : null);
                practitionerQualififcationTO.setState(qualificationDetailsMaster.getState() != null ? new BigInteger(qualificationDetailsMaster.getState().getIsoCode()) : null);
                practitionerQualififcationTO.setUniversity(qualificationDetailsMaster.getUniversity() != null ? qualificationDetailsMaster.getUniversity().getId() : null);
                practitionerQualififcationTO.setCollege(qualificationDetailsMaster.getCollege() != null ? qualificationDetailsMaster.getCollege().getId() : null);
                practitionerQualififcationTO.setMonthOfAwardingDegreeDiploma(qualificationDetailsMaster.getQualificationMonth());
                practitionerQualififcationTO.setYearOfAwardingDegreeDiploma(qualificationDetailsMaster.getQualificationYear());
                practitionerQualififcationTO.setIsNameDifferentInCertificate(BigInteger.ZERO);
                CertificateTO registrationCertificateTO = new CertificateTO();
                if (qualificationDetailsMaster.getCertificate() != null && StringUtils.isNotBlank(qualificationDetailsMaster.getFileName())) {
                    try {
                        registrationCertificateTO.setData(Base64.getEncoder().encodeToString(qualificationDetailsMaster.getCertificate()));
                        registrationCertificateTO.setFileType(Files.probeContentType(Paths.get(qualificationDetailsMaster.getFileName())));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    practitionerQualififcationTO.setDegreeCertificate(registrationCertificateTO);
                }
            });
        }
        if (foreignQualificationDetailsMasterList != null && !foreignQualificationDetailsMasterList.isEmpty()) {
            foreignQualificationDetailsMasterList.forEach(foreignQualificationDetailsMaster -> {
                practitionerQualififcationTO.setNameOfDegreeOrDiplomaObtained(BigInteger.ZERO);
                practitionerQualififcationTO.setCountry(BigInteger.ZERO);
                practitionerQualififcationTO.setState(BigInteger.ZERO);
                practitionerQualififcationTO.setUniversity(BigInteger.ZERO);
                practitionerQualififcationTO.setCollege(BigInteger.ZERO);
                practitionerQualififcationTO.setMonthOfAwardingDegreeDiploma(foreignQualificationDetailsMaster.getQualificationMonth());
                practitionerQualififcationTO.setYearOfAwardingDegreeDiploma(foreignQualificationDetailsMaster.getQualificationYear());
                practitionerQualififcationTO.setIsNameDifferentInCertificate(BigInteger.ZERO);
            });
        }
    }

    /**
     * Work details are optional in Council and  will be provided only after the NMR ID creation
     * As the HPR profile is created immediately after NMR ID creation, work details information are not passed to HPR.
     * Health - professional will need to declare the same by login in HPR.
     */
    private PractitionerWorkDetails populatePractitionerWorkDetails() {
        PractitionerWorkDetails practitionerWorkDetails = new PractitionerWorkDetails();
        practitionerWorkDetails.setCurrentlyWorking(BigInteger.ZERO);
        practitionerWorkDetails.setReasonForNotWorking("NA");
        return practitionerWorkDetails;
    }


}
