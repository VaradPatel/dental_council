package in.gov.abdm.nmr.db.sql.domain.college_registrar;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICollegeRegistrarRepository extends JpaRepository<CollegeRegistrar, BigInteger> {

}
