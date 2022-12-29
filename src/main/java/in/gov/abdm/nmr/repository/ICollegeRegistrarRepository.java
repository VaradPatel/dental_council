package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.CollegeRegistrar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeRegistrarRepository extends JpaRepository<CollegeRegistrar, BigInteger> {

    @Query(value = "SELECT c FROM collegeRegistrar c join c.user usr where usr.id=:userDetailId")
    CollegeRegistrar findByUserDetail(BigInteger userDetailId);
}
