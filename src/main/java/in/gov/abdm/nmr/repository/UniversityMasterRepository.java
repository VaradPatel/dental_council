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

    @Query(value = """
            select um.id,university_id,um.name,um.status,um.visible_status,um.college_id,um.created_at, um.updated_at 
            FROM main.college_master cm join main.university_master 
            um on cm.college_id=um.college_id where cm.state_id= :stateId""", nativeQuery = true)
    List<UniversityMaster> getUniversitiesByState(BigInteger stateId);

    @Query(value = "SELECT * FROM university_master WHERE id=:id", nativeQuery = true)
    UniversityMaster findUniversityMasterById(BigInteger id);

    @Query(value = "SELECT *  FROM university_master", nativeQuery = true)
    List<UniversityMaster> getUniversities();

    @Query(value = "SELECT id, name, status, visible_status, college_id, created_at, updated_at FROM university_master WHERE name= :universityName",
            nativeQuery = true)
    UniversityMaster findUniversityByName(String universityName);
}
