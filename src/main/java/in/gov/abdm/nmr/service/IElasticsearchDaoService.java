package in.gov.abdm.nmr.service;

import java.io.IOException;
import java.math.BigInteger;

import org.springframework.data.domain.Pageable;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResultTO;

public interface IElasticsearchDaoService {

    void indexHP(BigInteger hpProfileId) throws ElasticsearchException, IOException;

    SearchResponse<HpSearchResultTO> searchHP(HpSearchRequestTO hpSearchRequestTO, Pageable pageable) throws ElasticsearchException, IOException;

    boolean doesHpExists(BigInteger hpprofileId) throws ElasticsearchException, IOException;
}
