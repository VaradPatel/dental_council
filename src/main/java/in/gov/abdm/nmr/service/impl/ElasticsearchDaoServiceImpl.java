package in.gov.abdm.nmr.service.impl;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import in.gov.abdm.nmr.dto.HpElasticDocumentTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import in.gov.abdm.nmr.repository.IElasticsearchRepository;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.IHpProfileMasterDaoService;
import in.gov.abdm.nmr.service.IRegistrationDetailMasterDaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

@Service
public class ElasticsearchDaoServiceImpl implements IElasticsearchDaoService {

    private IElasticsearchRepository elasticsearchRepository;
    @Autowired
    private IHpProfileMasterDaoService iHpProfileMasterDaoService;
    @Autowired
    private IRegistrationDetailMasterDaoService iRegistrationDetailMasterDaoService;

    public ElasticsearchDaoServiceImpl(IElasticsearchRepository elasticsearchRepository, IHpProfileMasterDaoService hpProfileMasterDaoService, IRegistrationDetailMasterDaoService registrationDetailMasterDaoService) {
        this.elasticsearchRepository = elasticsearchRepository;
        this.iHpProfileMasterDaoService = hpProfileMasterDaoService;
        this.iRegistrationDetailMasterDaoService = registrationDetailMasterDaoService;
    }

    @Override
    public void indexHP(BigInteger hpProfileId) throws ElasticsearchException, IOException {

        HpProfileMaster hpProfile = iHpProfileMasterDaoService.findHpProfileMasterById(hpProfileId);

        HpElasticDocumentTO elasticDocumentTO = new HpElasticDocumentTO();
        elasticDocumentTO.setProfileId(hpProfile.getId());
        elasticDocumentTO.setFullName(hpProfile.getFullName());
        elasticDocumentTO.setSalutation(hpProfile.getSalutation());
        elasticDocumentTO.setProfileStatusId(hpProfile.getHpProfileStatus().getId());
        elasticDocumentTO.setProfilePhoto(hpProfile.getProfilePhoto());

        RegistrationDetailsMaster registrationDetails = iRegistrationDetailMasterDaoService.findByHpProfileId(hpProfileId);
        elasticDocumentTO.setRegistrationNumber(registrationDetails.getRegistrationNo());
        elasticDocumentTO.setRegistrationYear(new SimpleDateFormat("yyyy").format(registrationDetails.getRegistrationDate()));
        elasticDocumentTO.setStateMedicalCouncil(registrationDetails.getStateMedicalCouncil().getName());
        elasticDocumentTO.setStateMedicalCouncilId(registrationDetails.getStateMedicalCouncil().getId());

        elasticsearchRepository.indexHP(elasticDocumentTO);
    }

    @Override
    public SearchResponse<HpSearchResultTO> searchHP(HpSearchRequestTO hpSearchRequestTO, Pageable pageable) throws ElasticsearchException, IOException {
        BoolQuery.Builder queryBuilder = QueryBuilders.bool().queryName("sort");

        if (StringUtils.isNotBlank(hpSearchRequestTO.getRegistrationNumber())) {
            queryBuilder.filter(m -> m.match(mq -> mq.field("registration_number").query(hpSearchRequestTO.getRegistrationNumber())));
        }

        if (StringUtils.isNotBlank(hpSearchRequestTO.getRegistrationYear())) {
            queryBuilder.filter(m -> m.match(mq -> mq.field("registration_year").query(hpSearchRequestTO.getRegistrationYear())));
        }

        if (hpSearchRequestTO.getStateMedicalCouncilId() != null) {
            queryBuilder.filter(m -> m.match(mq -> mq.field("state_medical_council_id").query(hpSearchRequestTO.getStateMedicalCouncilId().longValueExact())));
        }

        if (hpSearchRequestTO.getProfileStatusId() != null) {
            queryBuilder.filter(m -> m.terms(mq -> mq.field("profile_status_id").terms(builder -> builder.value(hpSearchRequestTO.getProfileStatusId()))));
        }
        
        if (hpSearchRequestTO.getFullName() != null && !hpSearchRequestTO.getFullName().isBlank()) {
            BoolQuery.Builder fullNameQueryBuilder = QueryBuilders.bool().minimumShouldMatch("1");
            fullNameQueryBuilder.should(mm -> mm.multiMatch(mmq -> mmq.fields("full_name", "full_name.phonetic_analyzed", "full_name.keyword").query(hpSearchRequestTO.getFullName())
                    .fuzziness("AUTO")));
            fullNameQueryBuilder.should(mm -> mm.multiMatch(mmq -> mmq.fields("full_name", "full_name.phonetic_analyzed").query(hpSearchRequestTO.getFullName())
                    .analyzer("keyword").type(TextQueryType.BoolPrefix)));
            queryBuilder.must(b -> b.bool(fullNameQueryBuilder.build()));
            
            queryBuilder.queryName("non_sort");
        }

        return elasticsearchRepository.searchHP(queryBuilder.build(), pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public boolean doesHpExists(BigInteger hpprofileId) throws ElasticsearchException, IOException {
        return elasticsearchRepository.doesHpExists(hpprofileId);
    }
}
