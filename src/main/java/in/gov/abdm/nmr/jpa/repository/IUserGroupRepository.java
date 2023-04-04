package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IUserGroupRepository extends JpaRepository<UserGroup, BigInteger> {

}
