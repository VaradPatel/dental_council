package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IUserRepository extends JpaRepository<User, BigInteger> {

    User findByUsername(String username);

    boolean existsByUsername(String username);
}
