package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.client.GatewayFClient;
import in.gov.abdm.nmr.client.HPRIDFClient;
import in.gov.abdm.nmr.dto.HPRIdTokenRequestTO;
import in.gov.abdm.nmr.dto.HPRIdTokenResponseTO;
import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.entity.AddressMaster;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import in.gov.abdm.nmr.mapper.NMRToHPRMapper;
import in.gov.abdm.nmr.service.impl.HPRRegisterProfessionalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HPRRegisterProfessionalServiceImplTest {
    @InjectMocks
    HPRRegisterProfessionalServiceImpl hprRegisterProfessionalService;
    @Mock
    private NMRToHPRMapper nmrToHPRMapper;
    @Mock
    private HPRIDFClient hprIdClient;
    @Mock
    private GatewayFClient gatewayFClient;


    @Test
    void createRequestPayloadForHPRProfileCreation() {
        when(gatewayFClient.sessions(any(SessionRequestTo.class))).thenReturn(getSessionResponse());
        when(hprIdClient.getTokensByHprId(anyString(), any(HPRIdTokenRequestTO.class))).thenReturn(getHprIdTokenResponse());
        when(nmrToHPRMapper.convertNmrDataToHprRequestTo(any(HPRIdTokenResponseTO.class), any(HpProfileMaster.class), any(RegistrationDetailsMaster.class), any(AddressMaster.class),
                any(List.class), any(List.class))).thenReturn(getPractitionerRequest());
        hprRegisterProfessionalService.createRequestPayloadForHPRProfileCreation(getHpProfile(), getMasterHpProfile(), getMasterRegistrationDetails(), getAddressMaster(), getQualificationDetailsMasters(), getForeignQualificationDetailsMasters());
        verify(gatewayFClient, Mockito.times(1)).sessions(any(SessionRequestTo.class));
    }
}