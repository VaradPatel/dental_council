package in.gov.abdm.nmr.service;

import java.util.List;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.jpa.entity.StateMedicalCouncil;

public interface IStateMedicalCouncilDaoService {

    List<StateMedicalCouncilTO> smcs();
    
    StateMedicalCouncil findbyState(String stateId);
}
