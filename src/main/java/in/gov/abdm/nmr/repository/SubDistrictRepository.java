package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.SubDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface SubDistrictRepository extends JpaRepository<SubDistrict, BigInteger> {

    @Query(value = "SELECT id,iso_code,initcap(name) name,district_code ,created_at,updated_at FROM sub_district where district_code=:district order by name asc", nativeQuery = true)
    List<SubDistrict> getSubDistrict(BigInteger district);

    SubDistrict findByName(String name);
}
