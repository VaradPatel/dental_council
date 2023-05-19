package in.gov.abdm.nmr.service;


import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.entity.HpNbeDetails;
import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.HpProfileRegistrationMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.HpRegistrationServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HpRegistrationServiceImplTest {

    @InjectMocks
    HpRegistrationServiceImpl hpRegistrationService;

    @Mock
    IHpProfileDaoService hpProfileDaoService;
    @Mock
    IAddressRepository iAddressRepository;
    @Mock
    IWorkFlowRepository workFlowRepository;
    private MockMultipartFile certificate;
    @Mock
    IRegistrationDetailRepository registrationDetailRepository;
    @Mock
    HpNbeDetailsRepository hpNbeDetailsRepository;
    @Mock
    IQualificationDetailRepository qualificationDetailRepository;
    @Mock
    HpProfileRegistrationMapper hpProfileRegistrationMapper;
    @Mock
    IForeignQualificationDetailRepository customQualificationDetailRepository;

    @BeforeEach
    void setup() {
        certificate = new MockMultipartFile("employee", null,
                "application/json", "{\"name\": \"Emp Name\"}".getBytes());
    }


    @Test
    void testFuzzyScore() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Govind Kedia", "Govind Dwarkadasji Kedia");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testFuzzyScoreLogic() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Sathish Chonde", "Sathish Udhavroa Chonde");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testFuzzyScoreImplementation() throws IOException {
        double fuzzyScore = hpRegistrationService.getFuzzyScore("Vijay Anthony Chandrasaekar", "Vijay Chandrasaekar");
        Assertions.assertTrue(fuzzyScore >= NMRConstants.FUZZY_MATCH_LIMIT);
    }

    @Test
    void testAddOrUpdateHpPersonalDetail() throws WorkFlowException, InvalidRequestException {
        when(hpProfileDaoService.updateHpPersonalDetails(any(BigInteger.class), any(HpPersonalUpdateRequestTO.class)))
                .thenReturn(new HpProfileUpdateResponseTO(204, "Record Added/Updated Successfully!", HP_ID));
        when(hpProfileDaoService.findById(any(BigInteger.class))).thenReturn(getHpProfile());
        when(iAddressRepository.getCommunicationAddressByHpProfileId(any(BigInteger.class), any(Integer.class))).thenReturn(getCommunicationAddress());
        when(workFlowRepository.findLastWorkFlowForHealthProfessional(any(BigInteger.class))).thenReturn(getWorkFlow());
        HpProfilePersonalResponseTO responseTO = hpRegistrationService.addOrUpdateHpPersonalDetail(ID, new HpPersonalUpdateRequestTO());
        assertEquals(ID, responseTO.getHpProfileId());
        assertEquals(REQUEST_ID, responseTO.getRequestId());
    }

    public static RegistrationDetails getRegistrationDetails() {
        RegistrationDetails registrationDetails = new RegistrationDetails();
        registrationDetails.setHpProfileId(getHpProfile());
        return registrationDetails;
    }

    public static QualificationDetails getQualificationDetails() {
        QualificationDetails qualificationDetails = new QualificationDetails();
        return qualificationDetails;
    }

    @Test
    void testAddOrUpdateHpRegistrationDetail() throws InvalidRequestException, NmrException {
        when(hpProfileDaoService.updateHpRegistrationDetails(any(BigInteger.class), any(HpRegistrationUpdateRequestTO.class),
                any(MultipartFile.class), any(MultipartFile.class)))
                .thenReturn(new HpProfileUpdateResponseTO(204, "Record Added/Updated Successfully!", HP_ID));
        when(registrationDetailRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class)))
                .thenReturn(getRegistrationDetails());
        when(hpNbeDetailsRepository.findByUserId(any(BigInteger.class))).thenReturn(getHPNbeDetails());
        when(qualificationDetailRepository.getQualificationDetailsByUserId(any(BigInteger.class)))
                .thenReturn(List.of(getQualificationDetails()));
        when(customQualificationDetailRepository.getQualificationDetailsByUserId(any(BigInteger.class)))
                .thenReturn(List.of(getForeignQualificationDetails()));
        when(hpProfileRegistrationMapper.convertEntitiesToRegistrationResponseTo(
                any(RegistrationDetails.class), any(HpNbeDetails.class),
                any(List.class), any(List.class)))
                .thenReturn(new HpProfileRegistrationResponseTO());
        HpProfileRegistrationResponseTO hpProfileRegistrationResponseTO = hpRegistrationService.addOrUpdateHpRegistrationDetail(ID, new HpRegistrationUpdateRequestTO(),
                certificate, certificate);
        assertEquals(HP_ID, hpProfileRegistrationResponseTO.getHpProfileId());
    }
}
