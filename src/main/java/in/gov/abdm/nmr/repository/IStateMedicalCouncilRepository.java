package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IStateMedicalCouncilRepository extends JpaRepository<StateMedicalCouncil, BigInteger> {

    StateMedicalCouncil findByState(String stateId);
}
