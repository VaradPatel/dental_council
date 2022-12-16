package in.gov.abdm.nmr.db.sql.domain.address_type;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAddressTypeRepository extends JpaRepository<AddressType, BigInteger> {
	
	@Query(value = "select id, address_type from address_type where id = :addressTypeId", nativeQuery = true)
	AddressType findByAddressTypeId(BigInteger addressTypeId);

}
