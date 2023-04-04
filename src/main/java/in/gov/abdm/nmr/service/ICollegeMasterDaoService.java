package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.jpa.entity.CollegeMaster;

public interface ICollegeMasterDaoService {

    List<CollegeMaster> getAllColleges();

    CollegeMaster findById(BigInteger id);

    CollegeMaster save(CollegeMaster collegeMaster);
}
