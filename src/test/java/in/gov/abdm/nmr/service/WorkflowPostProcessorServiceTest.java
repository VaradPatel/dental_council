package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.entity.AddressMaster;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.WorkflowPostProcessorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowPostProcessorServiceTest {

    @InjectMocks
    WorkflowPostProcessorServiceImpl workflowPostProcessorService;
    @Mock
    IHpProfileRepository hpProfileRepository;

    @Mock
    IHpProfileMasterRepository hpProfileMasterRepository;

    @Mock
    IHpProfileMasterMapper hpProfileMasterMapper;

    @Mock
    IAddressMasterMapper addressMasterMapper;

    @Mock
    IForeignQualificationDetailsMasterMapper customQualificationDetailsMasterMapper;

    @Mock
    IRegistrationDetailRepository registrationDetailRepository;

    @Mock
    RegistrationDetailMasterRepository registrationDetailAuditRepository;

    @Mock
    IAddressRepository addressRepository;

    @Mock
    IAddressMasterRepository addressMasterRepository;

    @Mock
    IForeignQualificationDetailRepository customQualificationDetailRepository;

    @Mock
    IForeignQualificationDetailMasterRepository customQualificationDetailMasterRepository;

    @Mock
    HpNbeDetailsRepository hpNbeDetailsRepository;

    @Mock
    IHpNbeMasterMapper iHpNbeMasterMapper;

    @Mock
    HpNbeDetailsMasterRepository hpNbeDetailsMasterRepository;

    @Mock
    LanguagesKnownRepository languagesKnownRepository;

    @Mock
    LanguagesKnownMasterRepository languagesKnownMasterRepository;

    @Mock
    ILanguagesKnownMasterMapper languagesKnownMasterMapper;

    @Mock
    INmrHprLinkageRepository nmrHprLinkageRepository;

    @Mock
    INmrHprLinkageMasterRepository nmrHprLinkageMasterRepository;

    @Mock
    INmrHprLinkageMasterMapper nmrHprLinkageMasterMapper;

    @Mock
    IQualificationDetailRepository qualificationDetailRepository;

    @Mock
    IForeignQualificationDetailRepository foreignQualificationDetailRepository;

    @Mock
    IQualificationDetailMasterRepository qualificationDetailMasterRepository;

    @Mock
    IQualificationDetailMasterMapper qualificationDetailMasterMapper;

    @Mock
    SuperSpecialityRepository superSpecialityRepository;

    @Mock
    SuperSpecialityMasterRepository superSpecialityMasterRepository;

    @Mock
    ISuperSpecialityMasterMapper superSpecialityMasterMapper;

    @Mock
    WorkProfileMasterRepository workProfileAuditRepository;

    @Mock
    WorkProfileRepository workProfileRepository;

    @Mock
    private IElasticsearchDaoService elasticsearchDaoService;

    @Mock
    IHpProfileStatusRepository hpProfileStatusRepository;

    @Mock
    IUserRepository userRepository;

    @Mock
    INotificationService notificationService;
    @Mock
    private IWorkFlowRepository iWorkFlowRepository;
    @Mock
    private IHPRRegisterProfessionalService ihprRegisterProfessionalService;



    @BeforeEach
    void setup() {
    }

    @Test
    void testPerformPostWorkflowUpdates() throws WorkFlowException {
        when(iWorkFlowRepository.findByRequestId(anyString())).thenReturn(getWorkFlow());
        when(hpProfileStatusRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHPProfileStatus()));
        when(hpProfileMasterRepository.findByRegistrationId(anyString())).thenReturn(getMasterHpProfile());
        when(hpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfile());
        when(hpProfileMasterRepository.save(any(HpProfileMaster.class))).thenReturn(getMasterHpProfile());
        when(customQualificationDetailRepository.getQualificationDetailsByUserId(any(BigInteger.class))).thenReturn(Arrays.asList(getForeignQualificationDetails()));
        when(customQualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(any(BigInteger.class))).thenReturn(Arrays.asList(getForeignQualificationDetailsMaster()));
        when(notificationService.sendNotificationForNMRCreation(anyString(), anyString())).thenReturn(new ResponseMessageTo());
        when(userRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId())));
       when(ihprRegisterProfessionalService.createRequestPayloadForHPRProfileCreation(
                any(HpProfile.class), any(HpProfileMaster.class), any(RegistrationDetailsMaster.class), any(AddressMaster.class),
                any(List.class), any(List.class))).thenReturn(getHprRequestTo());

        workflowPostProcessorService.performPostWorkflowUpdates(getWorkFlowRequestTO(), getHpProfile(), getNextGroup());
        Mockito.verify(iWorkFlowRepository, Mockito.times(2)).findByRequestId(any(String.class));
    }

    @Test
    void testUpdateElasticDB() throws WorkFlowException, IOException {
        elasticsearchDaoService.indexHP(ID);
        workflowPostProcessorService.updateElasticDB(getWorkFlow(), getMasterHpProfile());
        verify(elasticsearchDaoService, times(2)).indexHP(any(BigInteger.class));
    }
}