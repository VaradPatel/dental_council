package in.gov.abdm.nmr.jpa.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.gov.abdm.nmr.jpa.entity.Password;

public interface IPasswordRepository extends JpaRepository<Password, BigInteger> {

    List<Password> findFirst5ByUserIdOrderByCreatedAtDesc(BigInteger userId);
}
