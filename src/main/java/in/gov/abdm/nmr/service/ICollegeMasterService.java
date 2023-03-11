package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CollegeMasterTo;

import java.math.BigInteger;
import java.util.List;

public interface ICollegeMasterService {

    List<CollegeMasterTo> getCollegesByStateId(BigInteger stateId);
}
