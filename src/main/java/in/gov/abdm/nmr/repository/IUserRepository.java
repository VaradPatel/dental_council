package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface IUserRepository extends JpaRepository<User, BigInteger> {

    @Query(value = """
            SELECT * FROM {h-schema}user usr WHERE usr.email = :username OR usr.mobile_number = :username
            OR usr.hpr_id = :username or usr.nmr_id= :username""", nativeQuery = true)
    User findByUsername(String username);

    User findFirstByEmail(String email);

    User findFirstByMobileNumber(String mobileNumber);

}
