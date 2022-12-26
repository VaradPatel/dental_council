package in.gov.abdm.nmr.db.sql.domain.address;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAddressRepository extends JpaRepository<Address, BigInteger> {
	
	@Query(value = "select * from address where hp_profile_id = :hpProfileId and address_type = :addressType", nativeQuery = true)
	Address getCommunicationAddressByHpProfileId(BigInteger hpProfileId, Integer addressType);
}
