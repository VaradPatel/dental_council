package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.NbeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface INbeProfileRepository extends JpaRepository<NbeProfile, BigInteger> {

    @Query(value = "SELECT nbe FROM nbeProfile nbe join nbe.user usr where usr.id=:userDetailId")
    NbeProfile findByUserDetail(BigInteger userDetailId);
}