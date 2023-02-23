package in.gov.abdm.nmr.repository;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import in.gov.abdm.nmr.dto.HpElasticDocumentTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;

import java.io.IOException;
import java.math.BigInteger;

public interface IElasticsearchRepository {

    void indexHP(HpElasticDocumentTO hpElasticDocumentTO) throws ElasticsearchException, IOException;

    SearchResponse<HpSearchResultTO> searchHP(BoolQuery query, int page, int size) throws ElasticsearchException, IOException;

    boolean doesHpExists(BigInteger hpprofileId) throws ElasticsearchException, IOException;
}
