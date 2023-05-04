package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.SuperSpeciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SuperSpecialityRepository extends JpaRepository<SuperSpeciality, BigInteger> {

    @Query(value = "SELECT name, id FROM super_speciality", nativeQuery = true)
    List<SuperSpeciality> getSpeciality();

}
