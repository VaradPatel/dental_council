package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.SuperSpecialityMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface SuperSpecialityMasterRepository extends JpaRepository<SuperSpecialityMaster, BigInteger> {


}
