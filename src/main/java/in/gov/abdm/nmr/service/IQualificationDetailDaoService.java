package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

public interface IQualificationDetailDaoService {

    List<Tuple> findSearchQualificationDetailsByHpProfileId(BigInteger hpprofileId);
}
