package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INationalityRepository extends JpaRepository<Nationality, BigInteger> {

}
