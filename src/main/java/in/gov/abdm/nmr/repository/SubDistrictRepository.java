package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.SubDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubDistrictRepository extends JpaRepository<SubDistrict, BigInteger> {

    @Query(value = "SELECT * FROM sub_district where district_code=:district", nativeQuery = true)
    List<SubDistrict> getSubDistrict(BigInteger district);

    SubDistrict findByName(String name);
}
