package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.RequestCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface IRequestCounterRepository extends JpaRepository<RequestCounter, BigInteger> {
}
