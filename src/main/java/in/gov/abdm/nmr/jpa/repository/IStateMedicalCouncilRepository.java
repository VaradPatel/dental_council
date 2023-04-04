package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.StateMedicalCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface IStateMedicalCouncilRepository extends JpaRepository<StateMedicalCouncil, BigInteger> {

    StateMedicalCouncil findByState(String stateId);

    @Query(value = "SELECT id, name, state, statename_r, created_at, updated_at, code, name_short, user_id FROM state_medical_council where id=:id", nativeQuery = true)
    StateMedicalCouncil findStateMedicalCouncilById(BigInteger id);
}
