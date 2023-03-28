package in.gov.abdm.nmr.repository;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import in.gov.abdm.nmr.entity.CollegeProfile;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeProfileRepository extends JpaRepository<CollegeProfile, BigInteger> {
    
    CollegeProfile findByCollegeId(BigInteger id);

    @Query(value = "SELECT c FROM collegeProfile c join c.user usr where usr.id=:userId")
    CollegeProfile findByUserId(BigInteger userId);
}
