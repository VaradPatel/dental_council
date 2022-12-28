package in.gov.abdm.nmr.db.sql.domain.user_detail;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<User, BigInteger> {

    User findByUsername(String username);
}
