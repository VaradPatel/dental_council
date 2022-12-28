package in.gov.abdm.nmr.db.sql.domain.state_medical_council;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.StateMedicalCouncilTO;

public interface IStateMedicalCouncilDaoService {

    List<StateMedicalCouncilTO> smcs();

    StateMedicalCouncil findByUserDetail(BigInteger userDetailId);
}
