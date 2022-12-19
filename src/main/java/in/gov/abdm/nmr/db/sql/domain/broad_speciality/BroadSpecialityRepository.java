package in.gov.abdm.nmr.db.sql.domain.broad_speciality;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BroadSpecialityRepository extends JpaRepository<BroadSpeciality, BigInteger> {

    @Query(value = "SELECT name, id FROM speciality", nativeQuery = true)
    List<BroadSpeciality> getSpeciality();

}
