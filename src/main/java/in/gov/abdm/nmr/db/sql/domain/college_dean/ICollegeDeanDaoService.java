package in.gov.abdm.nmr.db.sql.domain.college_dean;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;

public interface ICollegeDeanDaoService {

    CollegeDean saveCollegeDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo);
    
    CollegeDean findCollegeDeanById(BigInteger id);
}
