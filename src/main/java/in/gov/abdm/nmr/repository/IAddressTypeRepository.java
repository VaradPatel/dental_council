package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAddressTypeRepository extends JpaRepository<AddressType, BigInteger> {
	
	@Query(value = "select id, address_type from address_type where id = :addressTypeId", nativeQuery = true)
	AddressType findByAddressTypeId(BigInteger addressTypeId);

}
