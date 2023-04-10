package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.StateMedicalCouncilStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IStateMedicalCouncilStatusRepository extends JpaRepository<StateMedicalCouncilStatus, BigInteger> {

}
