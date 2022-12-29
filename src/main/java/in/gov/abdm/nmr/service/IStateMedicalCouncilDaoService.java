package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;

public interface IStateMedicalCouncilDaoService {

    List<StateMedicalCouncilTO> smcs();

    StateMedicalCouncil findByUserDetail(BigInteger userDetailId);
}
