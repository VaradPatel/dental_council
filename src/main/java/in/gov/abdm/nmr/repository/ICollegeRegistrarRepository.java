package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.CollegeRegistrar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static in.gov.abdm.nmr.entity.WorkFlow_.USER_ID;

public interface ICollegeRegistrarRepository extends JpaRepository<CollegeRegistrar, BigInteger> {

    @Query(value = "SELECT c FROM collegeRegistrar c join c.user usr where usr.id=:userDetailId")
    CollegeRegistrar findByUserDetail(BigInteger userDetailId);

    @Query(value = "SELECT cr.id FROM collegeRegistrar cr WHERE cr.user.id =:userId")
    List<BigInteger> getCollegeRegistrarIdByUserId(@Param(USER_ID) BigInteger userId);
}
