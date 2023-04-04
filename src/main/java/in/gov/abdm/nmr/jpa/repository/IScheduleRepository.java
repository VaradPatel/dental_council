package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IScheduleRepository extends JpaRepository<Schedule, BigInteger> {

}
