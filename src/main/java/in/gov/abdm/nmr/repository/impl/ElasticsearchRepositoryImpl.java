package in.gov.abdm.nmr.repository.impl;

import java.io.IOException;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import in.gov.abdm.nmr.dto.HpElasticDocumentTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;
import in.gov.abdm.nmr.repository.IElasticsearchRepository;

@Repository
public class ElasticsearchRepositoryImpl implements IElasticsearchRepository {

    private ElasticsearchClient elasticsearchClient;

    private String hpIndexName;

    public ElasticsearchRepositoryImpl(ElasticsearchClient elasticsearchClient, @Value("${nmr.elastic.index.hp}") String hpIndexName) {
        this.elasticsearchClient = elasticsearchClient;
        this.hpIndexName = hpIndexName;
    }

    @Override
    public void indexHP(HpElasticDocumentTO hpElasticDocumentTO) throws ElasticsearchException, IOException {
        elasticsearchClient.index(i -> i.index(hpIndexName).document(hpElasticDocumentTO).id(hpElasticDocumentTO.getProfileId().toString()));
    }

    @Override
    public SearchResponse<HpSearchResultTO> searchHP(BoolQuery query, int page, int size) throws ElasticsearchException, IOException {
        return elasticsearchClient.search(SearchRequest.of(s -> {
            Builder searchFunctionBuilder = s.index(hpIndexName).query(q -> q.bool(query)).from(page != 0 ? (page * size) : page).size(size);
            if("sort".equals(query.queryName())) {
                searchFunctionBuilder.sort(sb -> sb.field(fs -> fs.field("full_name.keyword").order(SortOrder.Asc)));
            }
            
            return searchFunctionBuilder;
        }), HpSearchResultTO.class);
    }

    @Override
    public boolean doesHpExists(BigInteger hpprofileId) throws ElasticsearchException, IOException {
        return elasticsearchClient.exists(ex -> ex.index(hpIndexName).id(hpprofileId.toString())).value();
    }
}
