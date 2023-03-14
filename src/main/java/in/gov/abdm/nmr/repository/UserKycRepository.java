package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserKyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface UserKycRepository extends JpaRepository<UserKyc, BigInteger> {

    UserKyc findUserKycById(BigInteger id);

    @Query(value = "SELECT * FROM user_kyc WHERE  registration_no=:registrationNo", nativeQuery = true)
    UserKyc findUserKycByRegistrationNumber(String registrationNo);
}
