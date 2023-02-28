package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.exception.NmrException;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;

public interface ISearchService {

    HpSearchResponseTO searchHP(HpSearchRequestTO hpSearchRequestTO, Pageable pageable) throws NmrException;

    HpSearchProfileTO getHpSearchProfileById(BigInteger profileId) throws NmrException;
}
