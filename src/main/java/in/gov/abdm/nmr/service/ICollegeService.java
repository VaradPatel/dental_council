package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.*;
import java.math.BigInteger;
import java.util.List;

public interface ICollegeService {

    List<CollegeMasterTo> getCollegesByStateId(BigInteger stateId);


    List<CollegeMasterTo> getCollegesByUniversity(BigInteger universityId);

}
