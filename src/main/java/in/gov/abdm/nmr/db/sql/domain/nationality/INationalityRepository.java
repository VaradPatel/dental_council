package in.gov.abdm.nmr.db.sql.domain.nationality;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface INationalityRepository extends JpaRepository<Nationality, BigInteger> {

}
