package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.StateMedicalCouncilStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStateMedicalCouncilStatusRepository extends JpaRepository<StateMedicalCouncilStatus, BigInteger> {

}
