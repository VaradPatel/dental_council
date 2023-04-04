package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface INationalityRepository extends JpaRepository<Nationality, BigInteger> {

}
