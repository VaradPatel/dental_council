package in.gov.abdm.nmr.service;

import java.util.List;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;

public interface IStateMedicalCouncilDaoService {

    List<StateMedicalCouncilTO> getAllStateMedicalCouncil();
    
    StateMedicalCouncil findByState(String stateId);
}
