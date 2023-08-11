package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.BroadSpeciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface BroadSpecialityRepository extends JpaRepository<BroadSpeciality, BigInteger> {

    @Query(value = "SELECT name, id FROM broad_speciality where visible_status = 1 and system_of_medicine_id = 1", nativeQuery = true)
    List<BroadSpeciality> getSpeciality();

}
