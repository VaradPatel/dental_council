package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IActionRepository extends JpaRepository<Action, BigInteger> {

}
