package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IHpProfileStatusRepository extends JpaRepository<HpProfileStatus, BigInteger> {
}
