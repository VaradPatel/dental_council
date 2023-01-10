package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IStateMedicalCouncilRepository extends JpaRepository<StateMedicalCouncil, BigInteger> {

    @Query(value = "SELECT smc FROM smcProfile smc join smc.user usr where usr.id=:userDetailId")
    StateMedicalCouncil findByUserDetail(BigInteger userDetailId);
}
