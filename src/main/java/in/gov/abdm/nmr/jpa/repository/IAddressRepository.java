package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;

public interface IAddressRepository extends JpaRepository<Address, BigInteger> {
	

	@Query(value = "select * from address where hp_profile_id = :hpProfileId and address_type_id = :addressType", nativeQuery = true)
	Address getCommunicationAddressByHpProfileId(BigInteger hpProfileId, Integer addressType);

	@Modifying
	@Transactional
	@Query(value = "UPDATE address SET email =:email WHERE hp_profile_id =:hpProfileId AND address_type_id = :addressType", nativeQuery = true)
	void updateAddressEmail(BigInteger hpProfileId, String email, Integer addressType);
}
