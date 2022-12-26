package in.gov.abdm.nmr.db.sql.domain.schedule;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IScheduleRepository extends JpaRepository<Schedule, BigInteger> {

}
