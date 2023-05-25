package in.gov.abdm.nmr.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.repository.IForeignQualificationDetailMasterRepository;
import in.gov.abdm.nmr.repository.IHpProfileMasterRepository;
import in.gov.abdm.nmr.repository.IQualificationDetailMasterRepository;
import in.gov.abdm.nmr.repository.RegistrationDetailMasterRepository;
import in.gov.abdm.nmr.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @InjectMocks
    SearchServiceImpl searchService;
    @Mock
    private IElasticsearchDaoService elasticsearchDaoService;
    @Mock
    private IHpProfileMasterRepository iHpProfileMasterRepository;
    @Mock
    private RegistrationDetailMasterRepository registrationDetailMasterRepository;
    @Mock
    private IQualificationDetailMasterRepository qualificationDetailMasterRepository;
    @Mock
    private IForeignQualificationDetailMasterRepository foreignQualificationDetailMasterRepository;


    @Test
    @WithMockUser
    void testGetHpSearchProfileByIdShouldReturnHealthProfessionalDetailsBaseOnHealthProfessionalId() throws IOException, NmrException, InvalidIdException {
        when(elasticsearchDaoService.doesHpExists(any(BigInteger.class))).thenReturn(true);
        when(iHpProfileMasterRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getMasterHpProfile()));
        when(registrationDetailMasterRepository.getRegistrationDetailsByHpProfileId(any(BigInteger.class)))
                .thenReturn(getMasterRegistrationDetails());

        when(qualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(any(BigInteger.class)))
                .thenReturn(getQualificationDetailsMasters());
        when(foreignQualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(any(BigInteger.class)))
                .thenReturn(getForeignQualificationDetailsMasters());
        HpSearchProfileTO hpSearchProfileById = searchService.getHpSearchProfileById(ID);
        assertEquals(PROFILE_DISPLAY_NAME, hpSearchProfileById.getFullName());
    }

    @Test
    @WithMockUser
    void testGetHpSearchProfileByIdShouldThrowInvalidIdException() throws IOException, NmrException, InvalidIdException {
        when(elasticsearchDaoService.doesHpExists(any(BigInteger.class))).thenReturn(false);
        assertThrows(InvalidIdException.class, () -> searchService.getHpSearchProfileById(ID));
    }


    @Test
    void testSearchHPShouldThrowException() throws InvalidRequestException {
        HpSearchRequestTO hpSearchRequestTO = new HpSearchRequestTO();
        hpSearchRequestTO.setRegistrationYear("2022");
        List<FieldValue> fieldValues = new ArrayList<>();
        fieldValues.add(FieldValue.of(2));
        hpSearchRequestTO.setProfileStatusId(fieldValues);
        assertThrows(Exception.class, () -> searchService.searchHP(hpSearchRequestTO, Pageable.ofSize(1)));
    }

}