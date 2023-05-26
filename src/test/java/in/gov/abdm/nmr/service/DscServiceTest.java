package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.client.DscFClient;
import in.gov.abdm.nmr.dto.dsc.AdditionalQualificationDocumentDetailsTO;
import in.gov.abdm.nmr.dto.dsc.DocumentDetailsTO;
import in.gov.abdm.nmr.dto.dsc.DscDocumentTo;
import in.gov.abdm.nmr.dto.dsc.DscRequestTo;
import in.gov.abdm.nmr.enums.TemplateEnum;
import in.gov.abdm.nmr.service.impl.DscServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DscServiceTest {

    @InjectMocks
    DscServiceImpl dscService;
    @Mock
    DscFClient dscFClient;

    @BeforeEach
    void setup() {
    }

    public static DscRequestTo getDscRequest() {
        DscRequestTo dscRequestTo = new DscRequestTo();
        dscRequestTo.setTemplateId(TemplateEnum.TEMPLATE_1.name());
        dscRequestTo.setSigningPlace("1");
        dscRequestTo.setDocumentDetailsTO(new DocumentDetailsTO());
        dscRequestTo.setAdditionalQualification(new AdditionalQualificationDocumentDetailsTO());
        return dscRequestTo;
    }

    @Test
    void testInvokeDSCGenEspRequestShouldThrowException() throws JsonProcessingException {
        when(dscFClient.genEspRequest(any(DscDocumentTo.class))).thenReturn("");
        assertThrows(Exception.class, () -> dscService.invokeDSCGenEspRequest(getDscRequest()));
    }

    @Test
    void testInvokeDSCGenEspRequestShouldInvokeDSCGenEspRequestWithTemplateIdNullThrowException() throws JsonProcessingException {
        when(dscFClient.genEspRequest(any(DscDocumentTo.class))).thenReturn("");
        assertThrows(Exception.class, () -> dscService.invokeDSCGenEspRequest(new DscRequestTo()));
    }

}