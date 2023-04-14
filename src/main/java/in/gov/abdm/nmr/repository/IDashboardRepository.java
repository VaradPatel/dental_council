package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;

public interface IDashboardRepository extends JpaRepository<Dashboard, Integer> {
}
