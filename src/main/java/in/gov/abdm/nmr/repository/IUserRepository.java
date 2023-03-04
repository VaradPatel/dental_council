package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.gov.abdm.nmr.entity.User;

public interface IUserRepository extends JpaRepository<User, BigInteger> {

    @Query(value = """
            SELECT * FROM {h-schema}user usr WHERE usr.email = :username OR usr.mobile_number = :username
            OR usr.hpr_id = :username or usr.nmr_id= :username""", nativeQuery = true)
    User findByUsername(String username);
}
