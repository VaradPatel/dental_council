package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IScheduleRepository extends JpaRepository<Schedule, BigInteger> {

}
