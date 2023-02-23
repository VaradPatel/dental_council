package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IUserGroupRepository extends JpaRepository<UserGroup, BigInteger> {

}
