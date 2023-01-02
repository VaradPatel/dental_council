package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeDeanDaoService {

    CollegeDean saveCollegeDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;
    
    CollegeDean findCollegeDeanById(BigInteger id);

    CollegeDean findByUserDetail(BigInteger userDetailId);
}