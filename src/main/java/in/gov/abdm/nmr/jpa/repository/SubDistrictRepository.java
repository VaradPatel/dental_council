package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.SubDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SubDistrictRepository extends JpaRepository<SubDistrict, BigInteger> {

    @Query(value = "SELECT * FROM sub_district where district_code=:district", nativeQuery = true)
    List<SubDistrict> getSubDistrict(BigInteger district);

    SubDistrict findByName(String name);
}
