package in.gov.abdm.nmr.repository;
import in.gov.abdm.nmr.entity.UniversityMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface UniversityMasterRepository extends JpaRepository<UniversityMaster, BigInteger> {
    @Query(value = "SELECT * FROM university_master WHERE  college_id=:collegeId", nativeQuery = true)
    List<UniversityMaster> getUniversitiesByCollegeId(BigInteger collegeId);

    @Query(value = "SELECT * FROM university_master WHERE id=:id", nativeQuery = true)
    UniversityMaster findUniversityMasterById(BigInteger id);

    @Query(value = "SELECT distinct on (name) name ,id,status ,visible_status ,college_id ,created_at ,updated_at FROM university_master", nativeQuery = true)
    List<UniversityMaster> getUniversities();

    @Query(value = "SELECT id, name, status, visible_status, college_id, created_at, updated_at FROM university_master WHERE name= :universityName",
            nativeQuery = true)
    UniversityMaster findUniversityByName(String universityName);
}
