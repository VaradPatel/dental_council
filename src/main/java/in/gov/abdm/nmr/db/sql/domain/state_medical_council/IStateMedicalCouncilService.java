package in.gov.abdm.nmr.db.sql.domain.state_medical_council;

import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.StateMedicalCouncilTO;

public interface IStateMedicalCouncilService {

    List<StateMedicalCouncilTO> smcs();
}
