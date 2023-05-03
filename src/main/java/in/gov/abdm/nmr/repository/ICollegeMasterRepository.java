package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.CollegeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface ICollegeMasterRepository extends JpaRepository<CollegeMaster, BigInteger> {
    @Query(value = "SELECT id, INITCAP(name) name, status, visible_status, system_of_medicine_id, state_id, course_id, created_at, updated_at, college_code, website, address_line_1, address_line_2, district_id, village_id, pin_code, state_medical_council_id FROM college_master WHERE state_id= :stateId order by name asc", nativeQuery = true)
    List<CollegeMaster> getCollegesByStateId(BigInteger stateId);

    @Query(value = "SELECT *  FROM college_master WHERE id= :id", nativeQuery = true)
    CollegeMaster findCollegeMasterById(BigInteger id);

    @Query(value = "SELECT id, INITCAP(name) name, status, visible_status, system_of_medicine_id, state_id, course_id, created_at, updated_at, college_code, website, address_line_1, address_line_2, district_id, village_id, pin_code, state_medical_council_id FROM college_master order by name asc", nativeQuery = true)
    List<CollegeMaster> getColleges();
    @Query(value = "SELECT *  FROM college_master WHERE name= :collegeName", nativeQuery = true)
    CollegeMaster getCollegeByName(String collegeName);
}
