package in.gov.abdm.nmr.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import in.gov.abdm.nmr.dto.HpElasticDocumentTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.repository.IElasticsearchRepository;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.IHpProfileDaoService;
import in.gov.abdm.nmr.service.IRegistrationDetailDaoService;

@Service
public class ElasticsearchDaoServiceImpl implements IElasticsearchDaoService {

    private IElasticsearchRepository elasticsearchRepository;

    private IHpProfileDaoService hpProfileDaoService;

    private IRegistrationDetailDaoService registrationDetailDaoService;

    public ElasticsearchDaoServiceImpl(IElasticsearchRepository elasticsearchRepository, IHpProfileDaoService hpProfileDaoService, IRegistrationDetailDaoService registrationDetailDaoService) {
        this.elasticsearchRepository = elasticsearchRepository;
        this.hpProfileDaoService = hpProfileDaoService;
        this.registrationDetailDaoService = registrationDetailDaoService;
    }

    @Override
    public void indexHP(BigInteger hpProfileId) throws ElasticsearchException, IOException {
        HpProfile hpprofile = hpProfileDaoService.findById(hpProfileId);

        HpElasticDocumentTO elasticDocumentTO = new HpElasticDocumentTO();
        elasticDocumentTO.setProfileId(hpprofile.getId());
        elasticDocumentTO.setFullName(hpprofile.getFullName());
        elasticDocumentTO.setSalutation(hpprofile.getSalutation());
        elasticDocumentTO.setProfileStatusId(hpprofile.getHpProfileStatus().getId());
        elasticDocumentTO.setProfilePhoto(hpprofile.getProfilePhoto() != null ? Base64.getEncoder().encodeToString(hpprofile.getProfilePhoto()) : "");

        RegistrationDetails registrationDetails = registrationDetailDaoService.findByHpProfileId(hpProfileId);
        elasticDocumentTO.setRegistrationNumber(registrationDetails.getRegistrationNo());
        elasticDocumentTO.setRegistrationYear(new SimpleDateFormat("yyyy").format(registrationDetails.getRegistrationDate()));
        elasticDocumentTO.setStateMedicalCouncil(registrationDetails.getStateMedicalCouncil().getName());
        elasticDocumentTO.setStateMedicalCouncilId(registrationDetails.getStateMedicalCouncil().getId());

        elasticsearchRepository.indexHP(elasticDocumentTO);
    }

    @Override
    public SearchResponse<HpSearchResultTO> searchHP(HpSearchRequestTO hpSearchRequestTO) throws ElasticsearchException, IOException {
        BoolQuery.Builder queryBuilder = QueryBuilders.bool();

        if (hpSearchRequestTO.getFullName() != null && !hpSearchRequestTO.getFullName().isBlank()) {
            BoolQuery.Builder fullNameQueryBuilder = QueryBuilders.bool().minimumShouldMatch("1");
            fullNameQueryBuilder.should(mm -> mm.multiMatch(mmq -> mmq.fields("full_name", "full_name.phonetic_analyzed", "full_name.keyword").query(hpSearchRequestTO.getFullName()) //
                    .fuzziness("AUTO")));
            fullNameQueryBuilder.should(mm -> mm.multiMatch(mmq -> mmq.fields("full_name", "full_name.phonetic_analyzed").query(hpSearchRequestTO.getFullName()) //
                    .analyzer("keyword").type(TextQueryType.BoolPrefix)));
            queryBuilder.must(b -> b.bool(fullNameQueryBuilder.build()));
        }

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
            queryBuilder.filter(m -> m.match(mq -> mq.field("profile_status_id").query(hpSearchRequestTO.getProfileStatusId().longValueExact())));
        }

        return elasticsearchRepository.searchHP(queryBuilder.build(), hpSearchRequestTO.getPage(), hpSearchRequestTO.getSize());
    }

    @Override
    public boolean doesHpExists(BigInteger hpprofileId) throws ElasticsearchException, IOException {
        return elasticsearchRepository.doesHpExists(hpprofileId);
    }
}
