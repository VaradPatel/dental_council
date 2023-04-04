package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IActionRepository extends JpaRepository<Action, BigInteger> {

}
