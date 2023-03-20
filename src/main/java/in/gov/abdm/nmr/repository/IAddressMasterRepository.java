package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.AddressMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;

public interface IAddressMasterRepository extends JpaRepository<AddressMaster, BigInteger> {

	@Query(value = "select * from address_master where hp_profile_id = :hpProfileId and address_type_id = :addressType", nativeQuery = true)
	AddressMaster getCommunicationAddressByHpProfileId(BigInteger hpProfileId, Integer addressType);

	@Modifying
	@Transactional
	@Query(value = "UPDATE address_master SET email =:email WHERE hp_profile_id =:masterHpProfileId AND address_type_id = :addressType", nativeQuery = true)
	void updateMasterAddressEmail(BigInteger masterHpProfileId, String email, Integer addressType);
}
