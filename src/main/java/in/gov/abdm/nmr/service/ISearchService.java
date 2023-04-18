package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidIDException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;

public interface ISearchService {

    HpSearchResponseTO searchHP(HpSearchRequestTO hpSearchRequestTO, Pageable pageable) throws NmrException, InvalidRequestException;

    HpSearchProfileTO getHpSearchProfileById(BigInteger profileId) throws NmrException, InvalidIDException;

    List fetchAddressByPinCodeFromLGD(String pinCode, String view);

}
