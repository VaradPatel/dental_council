package in.gov.abdm.nmr.db.sql.domain.college_dean;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeDeanDaoService {

    CollegeDean saveCollegeDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;
    
    CollegeDean findCollegeDeanById(BigInteger id);

    CollegeDean findByUserDetail(BigInteger userDetailId);
}
