package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.exception.NmrException;

import java.math.BigInteger;

public interface ICollegeDeanDaoService {

    CollegeDean saveCollegeDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;

    CollegeDean findCollegeDeanById(BigInteger id) throws NmrException;

    CollegeDean findByUserDetail(BigInteger userDetailId);
}
