package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.HpProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IHpProfileStatusRepository extends JpaRepository<HpProfileStatus, BigInteger> {
}
