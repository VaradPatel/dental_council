package in.gov.abdm.nmr.db.sql.domain.sub_district;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubDistrictRepository extends JpaRepository<SubDistrict, Long> {

    @Query(value = "SELECT * FROM sub_district where district=:district", nativeQuery = true)
    List<SubDistrict> getSubDistrict(Long district);

}
