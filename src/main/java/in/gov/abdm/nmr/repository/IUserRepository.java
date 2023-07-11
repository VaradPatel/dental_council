package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;
import static in.gov.abdm.nmr.util.NMRConstants.UPDATE_LAST_LOGIN;

public interface IUserRepository extends JpaRepository<User, BigInteger> {

    @Query(value = """
            SELECT * FROM {h-schema}user usr WHERE (usr.email = :username OR usr.mobile_number = :username
            OR usr.user_name = :username or usr.nmr_id= :username) AND usr.user_type_id=:userType""", nativeQuery = true)
    User findByUsername(String username, BigInteger userType);

    boolean existsByUserNameAndUserTypeId(String userName, BigInteger userTypeId);

    boolean existsByMobileNumberAndUserTypeId(String mobileNumber, BigInteger userTypeId);

    boolean existsByEmailAndUserTypeId(String email, BigInteger userTypeId);

    User findFirstByMobileNumber(String mobileNumber);

    @Query(value = "select count(*)>0 from {h-schema}user where id!=:id and email=:email and user_type_id=:userType", nativeQuery = true)
    boolean checkEmailUsedByOtherUser(BigInteger id,String email, BigInteger userType);

    @Query(value = "select count(*)>0 from {h-schema}user where id!=:id and mobile_number=:mobileNumber and user_type_id=:userType", nativeQuery = true)
    boolean checkMobileUsedByOtherUser(BigInteger id,String mobileNumber, BigInteger userType);

    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query(nativeQuery = true, value = DEACTIVATE_USER)
    void deactivateUser(@Param(USER_ID) BigInteger userId);

    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query(nativeQuery = true, value = UNLOCK_USER)
    void unlockUser(@Param(USER_ID) BigInteger userId);

    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query(nativeQuery = true, value = UPDATE_LAST_LOGIN)
    void updateLastLogin(BigInteger userId);

    @Query(value = """
            select user_name from {h-schema}user where mobile_number =:mobileNumber and user_type_id =:userType """, nativeQuery = true)
    List<String> getUserNamesByMobileNumAnduserType(String mobileNumber, BigInteger userType);
}
