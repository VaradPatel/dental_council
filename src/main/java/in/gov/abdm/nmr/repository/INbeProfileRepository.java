package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.NbeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.entity.WorkFlow_.USER_ID;

public interface INbeProfileRepository extends JpaRepository<NbeProfile, BigInteger> {

    @Query(value = "SELECT nbe FROM nbeProfile nbe join nbe.user usr where usr.id=:userDetailId")
    NbeProfile findByUserDetail(BigInteger userDetailId);

    @Query(value = "SELECT nbe.id FROM nbeProfile nbe WHERE nbe.user.id =:userId")
    List<BigInteger> getNbeProfileIdByUserId(@Param(USER_ID) BigInteger userId);
    @Query(value = "SELECT nbe FROM nbeProfile nbe join nbe.user usr where usr.id=:userId")
    NbeProfile findByUserId(BigInteger userId);
}