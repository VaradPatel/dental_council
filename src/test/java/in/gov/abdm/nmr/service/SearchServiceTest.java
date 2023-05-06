package in.gov.abdm.nmr.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.entity.UniversityMaster;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IUniversityMasterToMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.SearchServiceImpl;
import in.gov.abdm.nmr.service.impl.UniversityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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


/*    @Test
    void testSearchHP() throws InvalidRequestException, IOException {
        when(elasticsearchDaoService.searchHP(new HpSearchRequestTO(), getPageable())).thenReturn((SearchResponse<HpSearchResultTO>) Arrays.asList(new HpSearchResultTO()));
        HpSearchResponseTO responseTO = searchService.searchHP(new HpSearchRequestTO(), getPageable());
        assertEquals(ID, responseTO.getCount());

    }*/


}