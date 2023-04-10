package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface INationalityRepository extends JpaRepository<Nationality, BigInteger> {

}
