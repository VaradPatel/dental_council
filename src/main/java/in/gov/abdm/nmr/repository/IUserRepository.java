package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface IUserRepository extends JpaRepository<User, BigInteger> {

    @Query(value = """
            SELECT * FROM {h-schema}user usr WHERE usr.email = :username OR usr.mobile_number = :username
            OR usr.user_name = :username or usr.nmr_id= :username""", nativeQuery = true)
    User findByUsername(String username);

    boolean existsByUserName(String userName);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    User findFirstByMobileNumber(String mobileNumber);

    @Query(value = "select count(*)>0 from {h-schema}user where id!=:id and email=:email", nativeQuery = true)
    boolean checkEmailUsedByOtherUser(BigInteger id,String email);

}
