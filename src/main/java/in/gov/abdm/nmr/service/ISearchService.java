package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.HpSearchProfileTO;
import in.gov.abdm.nmr.dto.HpSearchRequestTO;
import in.gov.abdm.nmr.dto.HpSearchResponseTO;
import in.gov.abdm.nmr.exception.NmrException;

public interface ISearchService {

    HpSearchResponseTO searchHP(HpSearchRequestTO hpSearchRequestTO) throws NmrException;

    HpSearchProfileTO getHpSearchProfileById(BigInteger profileId) throws NmrException;
}
