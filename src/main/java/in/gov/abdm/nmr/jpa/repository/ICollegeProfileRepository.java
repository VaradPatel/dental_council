package in.gov.abdm.nmr.jpa.repository;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import in.gov.abdm.nmr.jpa.entity.CollegeProfile;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeProfileRepository extends JpaRepository<CollegeProfile, BigInteger> {
    
    @Query(value = "SELECT c FROM collegeProfile c where c.college.id = :collegeId and c.user.userSubType.id = :userSubTypeId")
    CollegeProfile findAdminByCollegeId(BigInteger collegeId, BigInteger userSubTypeId);

    @Query(value = "SELECT c FROM collegeProfile c join c.user usr where usr.id=:userId")
    CollegeProfile findByUserId(BigInteger userId);
}
