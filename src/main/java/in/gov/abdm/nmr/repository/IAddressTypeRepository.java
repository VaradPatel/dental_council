package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IAddressTypeRepository extends JpaRepository<AddressType, BigInteger> {

}
