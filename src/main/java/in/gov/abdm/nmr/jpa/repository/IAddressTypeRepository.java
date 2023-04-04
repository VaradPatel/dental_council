package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface IAddressTypeRepository extends JpaRepository<AddressType, BigInteger> {
	
	@Query(value = "select id, address_type from address_type where id = :addressTypeId", nativeQuery = true)
	AddressType findByAddressTypeId(BigInteger addressTypeId);

}
