package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.SuperSpecialityMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SuperSpecialityMasterRepository extends JpaRepository<SuperSpecialityMaster, BigInteger> {

    @Query(value = "SELECT name, id, hp_profile_id FROM super_speciality_master where hp_profile_id = :hpProfileId", nativeQuery = true)
    List<SuperSpecialityMaster> getSuperSpecialityFromHpProfileId(BigInteger hpProfileId);


}
