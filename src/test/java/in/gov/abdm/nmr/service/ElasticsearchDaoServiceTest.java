package in.gov.abdm.nmr.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import in.gov.abdm.nmr.dto.HpElasticDocumentTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.repository.IElasticsearchRepository;
import in.gov.abdm.nmr.service.impl.ElasticsearchDaoServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElasticsearchDaoServiceTest {
    @Mock
    private IElasticsearchRepository elasticsearchRepository;
    @Mock
    private IHpProfileMasterDaoService iHpProfileMasterDaoService;
    @Mock
    private IRegistrationDetailMasterDaoService iRegistrationDetailMasterDaoService;
    @Mock
    SearchResponse searchResponse;
    @InjectMocks
    ElasticsearchDaoServiceImpl elasticsearchDaoService;

    @Test
    void testIndexHpUpdateElasticIndex() throws IOException {
        when(iHpProfileMasterDaoService.findHpProfileMasterById(any(BigInteger.class))).thenReturn(getMasterHpProfile());
        when(iRegistrationDetailMasterDaoService.findByHpProfileId(any(BigInteger.class))).thenReturn(getMasterRegistrationDetails());
        doNothing().when(elasticsearchRepository).indexHP(any(HpElasticDocumentTO.class));
        elasticsearchDaoService.indexHP(ID);
        verify(elasticsearchRepository, times(1)).indexHP(any(HpElasticDocumentTO.class));
    }

    @Test
    void testSearchHpShouldReturnMatchingProfileBasedOnName() throws IOException {
        HpSearchRequestTO hpSearchRequestTO =  new HpSearchRequestTO();
        hpSearchRequestTO.setFullName(PROFILE_DISPLAY_NAME);
        when(elasticsearchRepository.searchHP(any(BoolQuery.class),anyInt(), anyInt())).thenReturn(searchResponse);
        elasticsearchDaoService.searchHP(hpSearchRequestTO, Pageable.ofSize(1));
        verify(elasticsearchRepository, times(1)).searchHP(any(BoolQuery.class),anyInt(), anyInt());

    }

    @Test
    void testSearchHpShouldReturnMatchingProfileBasedOnRegistrationNumber() throws IOException {
        HpSearchRequestTO hpSearchRequestTO =  new HpSearchRequestTO();
        hpSearchRequestTO.setRegistrationNumber(REGISTRATION_NUMBER);
        when(elasticsearchRepository.searchHP(any(BoolQuery.class),anyInt(), anyInt())).thenReturn(searchResponse);
        elasticsearchDaoService.searchHP(hpSearchRequestTO, Pageable.ofSize(1));
        verify(elasticsearchRepository, times(1)).searchHP(any(BoolQuery.class),anyInt(), anyInt());

    }

    @Test
    void testSearchHpShouldReturnMatchingProfileBasedOnStateMedicalCouncil() throws IOException {
        HpSearchRequestTO hpSearchRequestTO =  new HpSearchRequestTO();
        hpSearchRequestTO.setStateMedicalCouncilId(getStateMedicalCouncil().getId());
        when(elasticsearchRepository.searchHP(any(BoolQuery.class),anyInt(), anyInt())).thenReturn(searchResponse);
        elasticsearchDaoService.searchHP(hpSearchRequestTO, Pageable.ofSize(1));
        verify(elasticsearchRepository, times(1)).searchHP(any(BoolQuery.class),anyInt(), anyInt());

    }

    @Test
    void testSearchHpShouldReturnMatchingProfileBasedOnProfileStatus() throws IOException {
        HpSearchRequestTO hpSearchRequestTO =  new HpSearchRequestTO();
        List<FieldValue> profileStatusIdList = new ArrayList<>();
        hpSearchRequestTO.setProfileStatusId(profileStatusIdList);
        when(elasticsearchRepository.searchHP(any(BoolQuery.class),anyInt(), anyInt())).thenReturn(searchResponse);
        elasticsearchDaoService.searchHP(hpSearchRequestTO, Pageable.ofSize(1));
        verify(elasticsearchRepository, times(1)).searchHP(any(BoolQuery.class),anyInt(), anyInt());
    }

    @Test
    void testSearchHpShouldReturnMatchingProfileBasedOnRegistrationYear() throws IOException {
        HpSearchRequestTO hpSearchRequestTO =  new HpSearchRequestTO();
        hpSearchRequestTO.setRegistrationYear("2022");
        when(elasticsearchRepository.searchHP(any(BoolQuery.class),anyInt(), anyInt())).thenReturn(searchResponse);
        elasticsearchDaoService.searchHP(hpSearchRequestTO, Pageable.ofSize(1));
        verify(elasticsearchRepository, times(1)).searchHP(any(BoolQuery.class),anyInt(), anyInt());
    }

    @Test
    void testDoesHpProfileExists() throws IOException {
        when(elasticsearchRepository.doesHpExists(any(BigInteger.class))).thenReturn(true);
        assertTrue(elasticsearchDaoService.doesHpExists(ID));
    }

    private HpSearchResultTO getHpSearchResultTO(){
        HpSearchResultTO searchResultTO =  new HpSearchResultTO();
        searchResultTO.setProfileId(ID);
        searchResultTO.setRegistrationNumber(REGISTRATION_NUMBER);
        searchResultTO.setStateMedicalCouncil(STATE_NAME);
        searchResultTO.setProfilePhoto(PROFILE_PHOTO);
        searchResultTO.setFullName(PROFILE_DISPLAY_NAME);
        searchResultTO.setSalutation(NMRConstants.SALUTATION_DR);
        return searchResultTO;
    }


}
