package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeDeanDaoService {

    CollegeDean saveCollegeDean(BigInteger collegeId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;
    CollegeDean updateCollegeDean(BigInteger collegeId,BigInteger deanId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;

    CollegeDean findCollegeDeanById(BigInteger collegeId, BigInteger deanId) throws NmrException;

    CollegeDean findByUserId(BigInteger userId);
}
