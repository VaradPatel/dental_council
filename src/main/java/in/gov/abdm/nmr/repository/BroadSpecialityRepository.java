package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.BroadSpeciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface BroadSpecialityRepository extends JpaRepository<BroadSpeciality, BigInteger> {

    @Query(value = "SELECT name, id FROM broad_speciality", nativeQuery = true)
    List<BroadSpeciality> getSpeciality();

}
