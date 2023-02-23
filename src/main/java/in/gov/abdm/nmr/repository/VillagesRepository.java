package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Villages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface VillagesRepository extends JpaRepository<Villages, BigInteger> {

    @Query(value = "SELECT * FROM villages where sub_districts_code=:sub_district", nativeQuery = true)
    List<Villages> getVillage(BigInteger sub_district);

    Villages findByName(String name);
}
