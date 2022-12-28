package in.gov.abdm.nmr.db.sql.domain.state_medical_council;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IStateMedicalCouncilRepository extends JpaRepository<StateMedicalCouncil, BigInteger> {

    @Query(value = "SELECT smc.* FROM state_medical_council smc inner join user_detail ud on smc.user_detail = ud.id where ud.id=:userDetailId", nativeQuery = true)
    StateMedicalCouncil findByUserDetail(BigInteger userDetailId);
}
