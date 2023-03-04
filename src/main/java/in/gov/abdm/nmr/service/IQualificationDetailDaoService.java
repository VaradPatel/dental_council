package in.gov.abdm.nmr.service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;

public interface IQualificationDetailDaoService {

    List<Tuple> findSearchQualificationDetailsByHpProfileId(BigInteger hpprofileId);
}
