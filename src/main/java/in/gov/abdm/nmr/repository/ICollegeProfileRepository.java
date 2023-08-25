package in.gov.abdm.nmr.repository;
import java.math.BigInteger;

import in.gov.abdm.nmr.entity.CollegeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeProfileRepository extends JpaRepository<CollegeProfile, BigInteger> {
    
    @Query(value = "SELECT c FROM collegeProfile c where c.college.id = :collegeId and c.user.userSubType.id = :userSubTypeId")
    CollegeProfile findAdminByCollegeId(BigInteger collegeId, BigInteger userSubTypeId);

    @Query(value = "SELECT c FROM collegeProfile c join c.user usr where usr.id=:userId")
    CollegeProfile findByUserId(BigInteger userId);

    @Query(value = "select cp.* from college_profile cp join main.qualification_details qd on cp.college_id =qd.college_id where cp.user_id = :userId and qd.hp_profile_id = :hpProfileId", nativeQuery = true)
    CollegeProfile isHPBelongsToLoginCollege(BigInteger userId, BigInteger hpProfileId);
}
