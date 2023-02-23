package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface IFetchDetailsByRegNoRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

}
