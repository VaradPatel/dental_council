package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.StateMedicalCouncilStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IStateMedicalCouncilStatusRepository extends JpaRepository<StateMedicalCouncilStatus, BigInteger> {

}
