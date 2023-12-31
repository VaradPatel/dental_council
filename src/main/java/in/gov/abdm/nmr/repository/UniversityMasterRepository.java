package in.gov.abdm.nmr.repository;
import in.gov.abdm.nmr.entity.UniversityMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface UniversityMasterRepository extends JpaRepository<UniversityMaster, BigInteger> {
    @Query(value = "SELECT distinct on (name) name,id,status ,visible_status ,college_id, created_at, updated_at FROM university_master WHERE  college_id=:collegeId and visible_status = 1", nativeQuery = true)
    List<UniversityMaster> getUniversitiesByCollegeId(BigInteger collegeId);

    @Query(value = "SELECT * FROM university_master WHERE id=:id", nativeQuery = true)
    UniversityMaster findUniversityMasterById(BigInteger id);

    @Query(value = "SELECT distinct on (name) name ,id,status ,visible_status ,college_id ,created_at ,updated_at FROM university_master where visible_status = 1", nativeQuery = true)
    List<UniversityMaster> getUniversities();

    @Query(value = "SELECT id, name, status, visible_status, college_id, created_at, updated_at FROM university_master WHERE name ilike :universityName and college_id=:collegeId and visible_status = 1 LIMIT 1",
            nativeQuery = true)
    UniversityMaster findUniversityByName(String universityName, BigInteger collegeId);
}
