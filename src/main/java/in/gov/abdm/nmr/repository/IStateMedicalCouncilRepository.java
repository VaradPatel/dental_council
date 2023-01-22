package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import in.gov.abdm.nmr.entity.StateMedicalCouncil;

public interface IStateMedicalCouncilRepository extends JpaRepository<StateMedicalCouncil, BigInteger> {



}
