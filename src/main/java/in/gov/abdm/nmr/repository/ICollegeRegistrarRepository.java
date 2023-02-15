package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.CollegeRegistrar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.entity.WorkFlow_.USER_ID;

public interface ICollegeRegistrarRepository extends JpaRepository<CollegeRegistrar, BigInteger> {

    @Query(value = "SELECT c FROM collegeRegistrar c join c.user usr where usr.id=:userDetailId")
    CollegeRegistrar findByUserDetail(BigInteger userDetailId);

    @Query(value = "SELECT cr.id FROM collegeRegistrar cr WHERE cr.user.id =:userId")
    List<BigInteger> getCollegeRegistrarIdByUserId(@Param(USER_ID) BigInteger userId);

    @Query(value = "select * from college_registrar WHERE id =:registrarId and college_id =:collegeId ", nativeQuery = true)
    CollegeRegistrar getCollegeRegistrarByIds(@Param("registrarId") BigInteger registrarId, @Param("collegeId") BigInteger collegeId);
}
