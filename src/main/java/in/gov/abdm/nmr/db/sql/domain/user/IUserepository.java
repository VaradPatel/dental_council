package in.gov.abdm.nmr.db.sql.domain.user;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserepository extends JpaRepository<User, BigInteger> {

    User findByUsername(String username);
}
