package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;

public interface IAddressRepository extends JpaRepository<Address, BigInteger> {
	

	@Query(value = "select * from address where hp_profile_id = :hpProfileId and address_type_id = :addressType order by id desc limit 1", nativeQuery = true)
	Address getCommunicationAddressByHpProfileId(BigInteger hpProfileId, Integer addressType);

}
