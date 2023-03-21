package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.entity.HpNbeDetails;
import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@UtilityClass
public final class HpProfileRegistrationMapper {


public static HpProfileRegistrationResponseTO convertEntitiesToRegistrationResponseTo(RegistrationDetails registrationDetails, HpNbeDetails nbeDetails, List<QualificationDetails> indianQualifications, List<ForeignQualificationDetails> internationalQualifications) {
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = new HpProfileRegistrationResponseTO();
        RegistrationDetailTO registrationDetailsTo = new RegistrationDetailTO();
        NbeResponseTo  nbeResponseTo =  new NbeResponseTo();
        List<QualificationDetailResponseTo> qualifications =  new ArrayList<>();
        if(registrationDetails != null) {
            registrationDetailsTo.setRegistrationDate(registrationDetails.getRegistrationDate());
            registrationDetailsTo.setRenewableRegistrationDate(registrationDetails.getRenewableRegistrationDate());
            registrationDetailsTo.setIsNameChange(registrationDetails.getIsNameChange());
            registrationDetailsTo.setRegistrationNumber(registrationDetails.getRegistrationNo());
            registrationDetailsTo.setStateMedicalCouncil(StateMedicalCouncilTO.builder().code(registrationDetails.getStateMedicalCouncil().getCode()).name(registrationDetails.getStateMedicalCouncil().getName()).build());
            registrationDetailsTo.setIsRenewable(registrationDetails.getIsRenewable());
            if(registrationDetails.getCertificate() != null) {
                registrationDetailsTo.setRegistrationCertificate(Base64.getEncoder().encodeToString(registrationDetails.getCertificate()));
            }
            if (registrationDetails.getFileName() != null) {
                String[] res = registrationDetails.getFileName().split("[.]", 0);
                registrationDetailsTo.setFileName(res[0]);
                registrationDetailsTo.setFileType(res[1]);
            }
        }
        if(nbeDetails != null) {
            nbeResponseTo.setResult(nbeDetails.getUserResult());
            nbeResponseTo.setId(nbeDetails.getId());
            nbeResponseTo.setMonth(nbeDetails.getMonthOfPassing());
            nbeResponseTo.setMarksObtained(nbeDetails.getMarksObtained());
            nbeResponseTo.setRollNo(nbeDetails.getRollNo());
            nbeResponseTo.setYear(nbeDetails.getYearOfPassing());
            nbeResponseTo.setPassportNumber(nbeDetails.getPassportNumber());
        }
        if(!indianQualifications.isEmpty()) {
            qualifications.addAll(indianQualifications.stream().map(indianQualification -> {
                QualificationDetailResponseTo qualificationDetailResponseTo = new QualificationDetailResponseTo();

                qualificationDetailResponseTo.setId(indianQualification.getId());
                qualificationDetailResponseTo.setQualificationYear(indianQualification.getQualificationYear());
                qualificationDetailResponseTo.setQualificationMonth(indianQualification.getQualificationMonth());
                qualificationDetailResponseTo.setIsNameChange(indianQualification.getIsNameChange());
                qualificationDetailResponseTo.setQualificationFrom(NMRConstants.INDIA);
                qualificationDetailResponseTo.setCountry(CountryTO.builder().id(indianQualification.getCountry().getId()).name(indianQualification.getCountry().getName()).nationality(indianQualification.getCountry().getNationality()).build());
                qualificationDetailResponseTo.setState(StateTO.builder().id(indianQualification.getState().getId()).name(indianQualification.getState().getName()).build());
                qualificationDetailResponseTo.setCourse(CourseTO.builder().id(indianQualification.getCourse().getId()).courseName(indianQualification.getCourse().getCourseName()).build());
                qualificationDetailResponseTo.setUniversity(UniversityTO.builder().id(indianQualification.getUniversity().getId()).name(indianQualification.getUniversity().getName()).build());
                qualificationDetailResponseTo.setCollege(CollegeTO.builder().id(indianQualification.getCollege().getId()).name(indianQualification.getCollege().getName()).build());
                if(indianQualification.getCertificate() != null) {
                    qualificationDetailResponseTo.setDegreeCertificate(Base64.getEncoder().encodeToString(indianQualification.getCertificate()));
                }
                if (indianQualification.getFileName()!=null){
                    String[] res = indianQualification.getFileName().split("[.]", 0);
                    qualificationDetailResponseTo.setFileName(res[0]);
                    qualificationDetailResponseTo.setFileType(res[1]);
                }

                return qualificationDetailResponseTo;
            }).toList());
        }
        if(!internationalQualifications.isEmpty()) {
            qualifications.addAll(internationalQualifications.stream().map(internationalQualification -> {
                QualificationDetailResponseTo qualificationDetailResponseTo = new QualificationDetailResponseTo();
                qualificationDetailResponseTo.setId(internationalQualification.getId());
                qualificationDetailResponseTo.setQualificationYear(internationalQualification.getQualificationYear());
                qualificationDetailResponseTo.setQualificationMonth(internationalQualification.getQualificationMonth());
                qualificationDetailResponseTo.setQualificationFrom(NMRConstants.INTERNATIONAL);
                qualificationDetailResponseTo.setCountry(CountryTO.builder().name(internationalQualification.getCountry()).build());
                qualificationDetailResponseTo.setState(StateTO.builder().name(internationalQualification.getState()).build());
                qualificationDetailResponseTo.setCourse(CourseTO.builder().courseName(internationalQualification.getCourse()).build());
                qualificationDetailResponseTo.setUniversity(UniversityTO.builder().name(internationalQualification.getUniversity()).build());
                qualificationDetailResponseTo.setCollege(CollegeTO.builder().name(internationalQualification.getCollege()).build());
                if(internationalQualification.getCertificate() != null) {
                    qualificationDetailResponseTo.setDegreeCertificate(Base64.getEncoder().encodeToString(internationalQualification.getCertificate()));
                }
                if (internationalQualification.getFileName() != null) {
                    String[] res = internationalQualification.getFileName().split("[.]", 0);
                    qualificationDetailResponseTo.setFileName(res[0]);
                    qualificationDetailResponseTo.setFileType(res[1]);
                }
                return qualificationDetailResponseTo;
            }).toList());
        }


        hpProfileRegistrationResponseTO.setRegistrationDetailTO(registrationDetailsTo);
        hpProfileRegistrationResponseTO.setNbeResponseTo(nbeResponseTo);
        hpProfileRegistrationResponseTO.setQualificationDetailResponseTos(qualifications);
        if (registrationDetails != null) {
            hpProfileRegistrationResponseTO.setRequestId(registrationDetails.getRequestId());
        }
        return  hpProfileRegistrationResponseTO;
    }
}
