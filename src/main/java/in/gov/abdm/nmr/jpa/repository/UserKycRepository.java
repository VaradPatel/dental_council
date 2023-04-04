package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.UserKyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface UserKycRepository extends JpaRepository<UserKyc, BigInteger> {

    UserKyc findUserKycById(BigInteger id);

    @Query(value = "SELECT * FROM user_kyc WHERE  registration_no=:registrationNo", nativeQuery = true)
    UserKyc findUserKycByRegistrationNumber(String registrationNo);
}
