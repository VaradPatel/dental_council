package in.gov.abdm.nmr.db.sql.domain.state_medical_council;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IStateMedicalCouncilRepository extends JpaRepository<StateMedicalCouncil, BigInteger> {

}
