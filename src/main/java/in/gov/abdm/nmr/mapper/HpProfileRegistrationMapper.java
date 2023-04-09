package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.repository.CountryRepository;
import in.gov.abdm.nmr.repository.CourseRepository;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

@Component
public class HpProfileRegistrationMapper {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryDtoMapper countryDtoMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseDtoMapper courseDtoMapper;

public HpProfileRegistrationResponseTO convertEntitiesToRegistrationResponseTo(RegistrationDetails registrationDetails, HpNbeDetails nbeDetails, List<QualificationDetails> indianQualifications, List<ForeignQualificationDetails> internationalQualifications) {
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = new HpProfileRegistrationResponseTO();
        RegistrationDetailTO registrationDetailsTo = new RegistrationDetailTO();
        NbeResponseTo  nbeResponseTo =  new NbeResponseTo();
        List<QualificationDetailResponseTo> qualifications =  new ArrayList<>();
        if(registrationDetails != null) {
            registrationDetailsTo.setRegistrationDate(registrationDetails.getRegistrationDate());
            registrationDetailsTo.setRenewableRegistrationDate(registrationDetails.getRenewableRegistrationDate());
            registrationDetailsTo.setIsNameChange(registrationDetails.getIsNameChange());
            registrationDetailsTo.setRegistrationNumber(registrationDetails.getRegistrationNo());
            registrationDetailsTo.setStateMedicalCouncil(StateMedicalCouncilTO.builder().id(registrationDetails.getStateMedicalCouncil().getId()).name(registrationDetails.getStateMedicalCouncil().getName()).build());
            registrationDetailsTo.setIsRenewable(registrationDetails.getIsRenewable());
            if(registrationDetails.getCertificate() != null) {
                registrationDetailsTo.setRegistrationCertificate( new String(Base64.getEncoder().encodeToString(registrationDetails.getCertificate())));
            }
            if (registrationDetails.getFileName() != null && !registrationDetails.getFileName().isEmpty()) {
                registrationDetailsTo.setFileName(registrationDetails.getFileName().substring(0,registrationDetails.getFileName().lastIndexOf(".")));
                registrationDetailsTo.setFileType(registrationDetails.getFileName().substring(registrationDetails.getFileName().lastIndexOf(".")+1));

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
                if (indianQualification.getFileName()!=null) {
                    qualificationDetailResponseTo.setFileName(indianQualification.getFileName().substring(0, indianQualification.getFileName().lastIndexOf(".")));
                    qualificationDetailResponseTo.setFileType(indianQualification.getFileName().substring(indianQualification.getFileName().lastIndexOf(".") + 1));
                }
                qualificationDetailResponseTo.setRequestId(indianQualification.getRequestId());
                qualificationDetailResponseTo.setIsVerified(indianQualification.getIsVerified());
                qualificationDetailResponseTo.setCreatedAt(indianQualification.getCreatedAt());

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

                Country country = countryRepository.findByName(internationalQualification.getCountry());
                qualificationDetailResponseTo.setCountry( country != null
                        ? countryDtoMapper.mapToCountryTO(country)
                        : CountryTO.builder().name(internationalQualification.getCountry()).build());

                qualificationDetailResponseTo.setState(internationalQualification.getState()!=null?StateTO.builder().name(internationalQualification.getState()).build():null);

                Course course = courseRepository.getByCourseName(internationalQualification.getCourse());
                qualificationDetailResponseTo.setCourse( course != null
                        ? courseDtoMapper.mapToCourseTO(course)
                        : CourseTO.builder().courseName(internationalQualification.getCourse()).build());

                qualificationDetailResponseTo.setUniversity(internationalQualification.getUniversity()!=null?UniversityTO.builder().name(internationalQualification.getUniversity()).build():null);
                qualificationDetailResponseTo.setCollege(CollegeTO.builder().name(internationalQualification.getCollege()).build());
                if(internationalQualification.getCertificate() != null) {
                    qualificationDetailResponseTo.setDegreeCertificate(Base64.getEncoder().encodeToString(internationalQualification.getCertificate()));
                }
                if (internationalQualification.getFileName() != null) {
                    qualificationDetailResponseTo.setFileName(internationalQualification.getFileName().substring(0, internationalQualification.getFileName().lastIndexOf(".")));
                    qualificationDetailResponseTo.setFileType(internationalQualification.getFileName().substring(internationalQualification.getFileName().lastIndexOf(".") + 1));
                }
                qualificationDetailResponseTo.setRequestId(internationalQualification.getRequestId());
                qualificationDetailResponseTo.setIsVerified(internationalQualification.getIsVerified());
                qualificationDetailResponseTo.setCreatedAt(internationalQualification.getCreatedAt());
                return qualificationDetailResponseTo;
            }).toList());
        }

        qualifications.sort(Comparator.comparing(QualificationDetailResponseTo::getCreatedAt));
        hpProfileRegistrationResponseTO.setRegistrationDetailTO(registrationDetailsTo);
        hpProfileRegistrationResponseTO.setNbeResponseTo(nbeResponseTo);
        hpProfileRegistrationResponseTO.setQualificationDetailResponseTos(qualifications);
        if (registrationDetails != null) {
            hpProfileRegistrationResponseTO.setRequestId(registrationDetails.getRequestId());
        }
        return  hpProfileRegistrationResponseTO;
    }
}
